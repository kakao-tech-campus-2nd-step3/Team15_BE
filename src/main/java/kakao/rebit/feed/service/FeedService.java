package kakao.rebit.feed.service;

import java.util.Optional;
import kakao.rebit.book.entity.Book;
import kakao.rebit.book.service.BookService;
import kakao.rebit.feed.dto.request.create.CreateFavoriteBookRequest;
import kakao.rebit.feed.dto.request.create.CreateFeedRequest;
import kakao.rebit.feed.dto.request.update.UpdateFavoriteBookRequest;
import kakao.rebit.feed.dto.request.update.UpdateFeedRequest;
import kakao.rebit.feed.dto.response.FeedResponse;
import kakao.rebit.feed.entity.Feed;
import kakao.rebit.feed.mapper.FeedMapper;
import kakao.rebit.feed.repository.FeedRepository;
import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.service.MemberService;
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

    public FeedService(FeedRepository feedRepository, MemberService memberService,
            BookService bookService, FeedMapper feedMapper) {
        this.feedRepository = feedRepository;
        this.memberService = memberService;
        this.bookService = bookService;
        this.feedMapper = feedMapper;
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
                .orElseThrow(() -> new IllegalArgumentException("찾는 피드가 존재하지 않습니다."));
    }

    @Transactional
    public Long createFeed(MemberResponse memberResponse, CreateFeedRequest feedRequest) {
        Member member = memberService.findMemberByIdOrThrow(memberResponse.id());

        // 인생책 검증 - 반드시 책이 있어야 된다.
        if (feedRequest instanceof CreateFavoriteBookRequest && feedRequest.getBookId() == null) {
            throw new IllegalArgumentException("인생책은 책이 반드시 필요합니다.");
        }
        Book book = findBookIfBookIdExist(feedRequest.getBookId()).orElse(null);
        Feed feed = feedMapper.toFeed(member, book, feedRequest);
        return feedRepository.save(feed).getId();
    }

    @Transactional
    public void updateFeed(MemberResponse memberResponse, Long feedId,
            UpdateFeedRequest feedRequest) {
        Member member = memberService.findMemberByIdOrThrow(memberResponse.id());
        Feed feed = findFeedByIdOrThrow(feedId);

        // 피드 작성자 확인
        if (!feed.isWrittenBy(member)) {
            throw new IllegalArgumentException("본인이 올린 피드만 수정할 수 있습니다.");
        }

        // 인생책 검증 - 반드시 책이 있어야 된다.
        if (feedRequest instanceof UpdateFavoriteBookRequest && feedRequest.getBookId() == null) {
            throw new IllegalArgumentException("인생책은 책이 반드시 필요합니다.");
        }

        Book book = findBookIfBookIdExist(feedRequest.getBookId()).orElse(null);
        feed.changeBook(book);
        feed.updateAllExceptBook(feedRequest);
    }

    @Transactional
    public void deleteFeedById(MemberResponse memberResponse, Long feedId) {
        Member member = memberService.findMemberByIdOrThrow(memberResponse.id());
        Feed feed = findFeedByIdOrThrow(feedId);

        if (!feed.isWrittenBy(member)) {
            throw new IllegalArgumentException("본인의 글만 삭제할 수 있습니다.");
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
