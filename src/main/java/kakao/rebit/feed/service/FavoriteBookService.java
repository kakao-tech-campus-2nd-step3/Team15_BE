package kakao.rebit.feed.service;

import java.util.Set;
import kakao.rebit.book.entity.Book;
import kakao.rebit.book.service.BookService;
import kakao.rebit.feed.dto.request.update.UpdateFavoriteBookRequest;
import kakao.rebit.feed.dto.response.FavoriteBookResponse;
import kakao.rebit.feed.entity.FavoriteBook;
import kakao.rebit.feed.exception.feed.FeedNotFoundException;
import kakao.rebit.feed.exception.feed.UpdateNotAuthorizedException;
import kakao.rebit.feed.mapper.FeedMapper;
import kakao.rebit.feed.repository.FavoriteBookRepository;
import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteBookService {

    private final FavoriteBookRepository favoriteBookRepository;
    private final BookService bookService;
    private final MemberService memberService;
    private final LikesService likesService;
    private final FeedMapper feedMapper;

    public FavoriteBookService(FavoriteBookRepository favoriteBookRepository, BookService bookService, MemberService memberService,
            LikesService likesService, FeedMapper feedMapper) {
        this.favoriteBookRepository = favoriteBookRepository;
        this.bookService = bookService;
        this.memberService = memberService;
        this.likesService = likesService;
        this.feedMapper = feedMapper;
    }

    @Transactional(readOnly = true)
    public Page<FavoriteBookResponse> getFavoriteBooks(MemberResponse memberResponse, Pageable pageable) {
        Page<FavoriteBook> feedPage = favoriteBookRepository.findAll(pageable);

        if (memberResponse != null) {
            Member viewer = memberService.findMemberByIdOrThrow(memberResponse.id());
            Set<Long> likedFeedIds = likesService.getLikedFeedIdsByMember(viewer); // 멤버가 좋아요를 누른 모든 피드를 가져온다.

            return feedPage.map(feed -> (FavoriteBookResponse)
                    feedMapper.toFeedResponse(likesService.isLikedBySet(likedFeedIds, feed), feed));
        }

        return feedPage.map(feed -> (FavoriteBookResponse) feedMapper.toFeedResponse(false, feed));
    }

    @Transactional(readOnly = true)
    public Page<FavoriteBookResponse> getMyFavoriteBooks(MemberResponse memberResponse, Pageable pageable) {
        Member author = memberService.findMemberByIdOrThrow(memberResponse.id());
        Set<Long> likedFeedIds = likesService.getLikedFeedIdsByMember(author);

        return favoriteBookRepository.findAllByMember(author, pageable)
                .map(feed -> (FavoriteBookResponse) feedMapper.toFeedResponse(likesService.isLikedBySet(likedFeedIds, feed), feed));
    }

    @Transactional(readOnly = true)
    public FavoriteBookResponse getFavoriteBookById(MemberResponse memberResponse, Long favoriteBookId) {
        Member viewer = memberService.findMemberByIdOrThrow(memberResponse.id());
        FavoriteBook favoriteBook = findFavoriteBookByIdOrThrow(favoriteBookId);
        return (FavoriteBookResponse) feedMapper.toFeedResponse(likesService.isLiked(viewer, favoriteBook), favoriteBook);
    }

    @Transactional(readOnly = true)
    public FavoriteBook findFavoriteBookByIdOrThrow(Long favoriteBookId) {
        return favoriteBookRepository.findById(favoriteBookId)
                .orElseThrow(() -> FeedNotFoundException.EXCEPTION);
    }

    @Transactional
    public void updateFavoriteBook(MemberResponse memberResponse, Long favoriteBookId, UpdateFavoriteBookRequest updateRequest) {
        Member author = memberService.findMemberByIdOrThrow(memberResponse.id());
        FavoriteBook favoriteBook = findFavoriteBookByIdOrThrow(favoriteBookId);

        if (!favoriteBook.isWrittenBy(author)) {
            throw UpdateNotAuthorizedException.EXCEPTION;
        }

        Book book = bookService.findBookByIdOrThrow(updateRequest.bookId());

        favoriteBook.changeBook(book);
        favoriteBook.updateTextFields(updateRequest.briefReview(), updateRequest.fullReview());
    }
}
