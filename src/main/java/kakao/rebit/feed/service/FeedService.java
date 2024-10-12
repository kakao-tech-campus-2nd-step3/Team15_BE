package kakao.rebit.feed.service;

import java.util.Optional;
import kakao.rebit.book.entity.Book;
import kakao.rebit.book.service.BookService;
import kakao.rebit.common.domain.ImageKeyHolder;
import kakao.rebit.feed.dto.request.create.CreateFavoriteBookRequest;
import kakao.rebit.feed.dto.request.create.CreateFeedRequest;
import kakao.rebit.feed.dto.response.FeedResponse;
import kakao.rebit.feed.entity.Feed;
import kakao.rebit.feed.exception.feed.DeleteNotAuthorizedException;
import kakao.rebit.feed.exception.feed.FavoriteBookRequiredBookException;
import kakao.rebit.feed.exception.feed.FeedNotFoundException;
import kakao.rebit.feed.mapper.FeedMapper;
import kakao.rebit.feed.repository.FeedRepository;
import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.service.MemberService;
import kakao.rebit.s3.service.S3Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedService {

    private final FeedRepository feedRepository;
    private final MemberService memberService;
    private final BookService bookService;
    private final FeedMapper feedMapper;
    private final S3Service s3Service;

    public FeedService(FeedRepository feedRepository, MemberService memberService,
            BookService bookService, FeedMapper feedMapper, S3Service s3Service) {
        this.feedRepository = feedRepository;
        this.memberService = memberService;
        this.bookService = bookService;
        this.feedMapper = feedMapper;
        this.s3Service = s3Service;
    }

    @Transactional(readOnly = true)
    public Page<FeedResponse> getFeeds(Pageable pageable) {
        return feedRepository.findAll(pageable).map(feedMapper::toFeedResponse);
    }

    @Transactional(readOnly = true)
    public FeedResponse getFeedById(Long feedId) {
        Feed feed = findFeedByIdOrThrow(feedId);
        return feedMapper.toFeedResponse(feed);
    }

    @Transactional(readOnly = true)
    public Feed findFeedByIdOrThrow(Long feedId) {
        return feedRepository.findById(feedId)
                .orElseThrow(() -> FeedNotFoundException.EXCEPTION);
    }

    @Transactional
    public Long createFeed(MemberResponse memberResponse, CreateFeedRequest feedRequest) {
        Member member = memberService.findMemberByIdOrThrow(memberResponse.id());

        // 인생책 검증 - 반드시 책이 있어야 된다.
        if (feedRequest instanceof CreateFavoriteBookRequest && feedRequest.getBookId() == null) {
            throw FavoriteBookRequiredBookException.EXCEPTION;
        }

        Book book = findBookIfBookIdExist(feedRequest.getBookId()).orElse(null);
        Feed feed = feedMapper.toFeed(member, book, feedRequest);
        return feedRepository.save(feed).getId();
    }

    @Transactional
    public void deleteFeedById(MemberResponse memberResponse, Long feedId) {
        Member member = memberService.findMemberByIdOrThrow(memberResponse.id());
        Feed feed = findFeedByIdOrThrow(feedId);

        if (!feed.isWrittenBy(member)) {
            throw DeleteNotAuthorizedException.EXCEPTION;
        }

        // 메거진과 스토리는 피드 삭제 전 S3에서 image 파일을 먼저 삭제한다.
        if (feed instanceof ImageKeyHolder imageKeyHolder) {
            s3Service.deleteObject(imageKeyHolder.getImageKey());
        }

        feedRepository.deleteById(feedId);
    }

    private Optional<Book> findBookIfBookIdExist(Long bookId) {
        if (bookId != null) {
            Book book = bookService.findBookByIdOrThrow(bookId);
            return Optional.of(book);
        }
        return Optional.empty();
    }
}
