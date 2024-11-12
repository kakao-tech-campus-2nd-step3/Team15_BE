package kakao.rebit.feed.service;

import java.util.Optional;
import java.util.Set;
import kakao.rebit.book.entity.Book;
import kakao.rebit.book.service.BookService;
import kakao.rebit.common.domain.ImageKeyAccessor;
import kakao.rebit.feed.dto.request.create.CreateFavoriteBookRequest;
import kakao.rebit.feed.dto.request.create.CreateFeedRequest;
import kakao.rebit.feed.dto.response.FeedResponse;
import kakao.rebit.feed.dto.response.LikesMemberResponse;
import kakao.rebit.feed.entity.Feed;
import kakao.rebit.feed.exception.feed.DeleteNotAuthorizedException;
import kakao.rebit.feed.exception.feed.FavoriteBookRequiredBookException;
import kakao.rebit.feed.exception.feed.FeedNotFoundException;
import kakao.rebit.feed.exception.likes.FindNotAuthorizedException;
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
    private final LikesService likesService;

    public FeedService(FeedRepository feedRepository, MemberService memberService, BookService bookService, FeedMapper feedMapper,
            S3Service s3Service,
            LikesService likesService) {
        this.feedRepository = feedRepository;
        this.memberService = memberService;
        this.bookService = bookService;
        this.feedMapper = feedMapper;
        this.s3Service = s3Service;
        this.likesService = likesService;
    }

    @Transactional(readOnly = true)
    public Page<FeedResponse> getFeeds(MemberResponse memberResponse, Pageable pageable) {
        Page<Feed> feedPage = feedRepository.findAll(pageable);

        if (memberResponse != null) {
            Member viewer = memberService.findMemberByIdOrThrow(memberResponse.id());
            Set<Long> likedFeedIds = likesService.getLikedFeedIdsByMember(viewer); // 멤버가 좋아요를 누른 모든 피드를 가져온다.

            return feedPage.map(feed ->
                    feedMapper.toFeedResponse(likesService.isLikedBySet(likedFeedIds, feed), feed));
        }

        return feedPage.map(feed -> feedMapper.toFeedResponse(false, feed));
    }

    @Transactional(readOnly = true)
    public Page<FeedResponse> getMyFeeds(MemberResponse memberResponse, Pageable pageable) {
        Member author = memberService.findMemberByIdOrThrow(memberResponse.id());
        Set<Long> likedFeedIds = likesService.getLikedFeedIdsByMember(author);

        return feedRepository.findAllByMember(author, pageable)
                .map(feed -> feedMapper.toFeedResponse(likesService.isLikedBySet(likedFeedIds, feed), feed));
    }

    @Transactional(readOnly = true)
    public FeedResponse getFeedById(MemberResponse memberResponse, Long feedId) {
        Member viewer = memberService.findMemberByIdOrThrow(memberResponse.id());
        Feed feed = findFeedByIdOrThrow(feedId);
        return feedMapper.toFeedResponse(likesService.isLiked(viewer, feed), feed);
    }

    @Transactional(readOnly = true)
    public Feed findFeedByIdOrThrow(Long feedId) {
        return feedRepository.findById(feedId)
                .orElseThrow(() -> FeedNotFoundException.EXCEPTION);
    }

    @Transactional
    public Long createFeed(MemberResponse memberResponse, CreateFeedRequest feedRequest) {
        Member author = memberService.findMemberByIdOrThrow(memberResponse.id());

        // 인생책 검증 - 반드시 책이 있어야 된다.
        if (feedRequest instanceof CreateFavoriteBookRequest && feedRequest.getBookId() == null) {
            throw FavoriteBookRequiredBookException.EXCEPTION;
        }

        Book book = findBookIfBookIdExist(feedRequest.getBookId()).orElse(null);
        Feed feed = feedMapper.toFeed(author, book, feedRequest);
        return feedRepository.save(feed).getId();
    }

    @Transactional
    public void deleteFeedById(MemberResponse memberResponse, Long feedId) {
        Member author = memberService.findMemberByIdOrThrow(memberResponse.id());
        Feed feed = findFeedByIdOrThrow(feedId);

        if (!feed.isWrittenBy(author)) {
            throw DeleteNotAuthorizedException.EXCEPTION;
        }

        feedRepository.deleteById(feedId);

        // 트랜잭션 롤백을 사용하기 위해 피드 삭제 후 S3에서 image 파일을 삭제한다.
        if (feed instanceof ImageKeyAccessor imageKeyAccessor) {
            s3Service.deleteObject(imageKeyAccessor.getImageKey());
        }
    }

    @Transactional(readOnly = true)
    public Page<LikesMemberResponse> getLikesMembers(MemberResponse memberResponse, Long feedId, Pageable pageable) {
        Member member = memberService.findMemberByIdOrThrow(memberResponse.id());
        Feed feed = findFeedByIdOrThrow(feedId);

        if (!feed.isWrittenBy(member)) {
            throw FindNotAuthorizedException.EXCEPTION;
        }
        return likesService.findLikesMembers(feed, pageable);
    }

    @Transactional
    public Long createLikes(MemberResponse memberResponse, Long feedId) {
        Member member = memberService.findMemberByIdOrThrow(memberResponse.id());
        Feed feed = findFeedByIdOrThrow(feedId);

        return likesService.createLikes(member, feed);
    }

    @Transactional
    public void deleteLikes(MemberResponse memberResponse, Long feedId) {
        Member member = memberService.findMemberByIdOrThrow(memberResponse.id());
        Feed feed = findFeedByIdOrThrow(feedId);

        likesService.deleteLikes(member, feed);
    }

    private Optional<Book> findBookIfBookIdExist(Long bookId) {
        if (bookId != null) {
            Book book = bookService.findBookByIdOrThrow(bookId);
            return Optional.of(book);
        }
        return Optional.empty();
    }
}
