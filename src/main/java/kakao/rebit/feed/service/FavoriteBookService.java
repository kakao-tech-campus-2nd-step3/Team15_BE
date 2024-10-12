package kakao.rebit.feed.service;

import kakao.rebit.book.entity.Book;
import kakao.rebit.book.service.BookService;
import kakao.rebit.feed.dto.request.update.UpdateFavoriteBookRequest;
import kakao.rebit.feed.dto.response.FeedResponse;
import kakao.rebit.feed.entity.FavoriteBook;
import kakao.rebit.feed.exception.feed.FavoriteBookRequiredBookException;
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
    private final FeedMapper feedMapper;

    public FavoriteBookService(FavoriteBookRepository favoriteBookRepository,
            BookService bookService,
            MemberService memberService, FeedMapper feedMapper) {
        this.favoriteBookRepository = favoriteBookRepository;
        this.bookService = bookService;
        this.memberService = memberService;
        this.feedMapper = feedMapper;
    }

    @Transactional(readOnly = true)
    public Page<FeedResponse> getFavoriteBooks(Pageable pageable) {
        Page<FavoriteBook> favorites = favoriteBookRepository.findAll(pageable);
        return favorites.map(feedMapper::toFeedResponse);
    }

    @Transactional(readOnly = true)
    public FeedResponse getFavoriteBookById(Long favoriteBookId) {
        FavoriteBook favoriteBook = findFavoriteBookByIdOrThrow(favoriteBookId);
        return feedMapper.toFeedResponse(favoriteBook);
    }

    @Transactional(readOnly = true)
    public FavoriteBook findFavoriteBookByIdOrThrow(Long favoriteBookId) {
        return favoriteBookRepository.findById(favoriteBookId)
                .orElseThrow(() -> FeedNotFoundException.EXCEPTION);
    }

    @Transactional
    public void updateFavoriteBook(MemberResponse memberResponse, Long favoriteBookId,
            UpdateFavoriteBookRequest updateRequest) {
        Member member = memberService.findMemberByIdOrThrow(memberResponse.id());
        FavoriteBook favoriteBook = findFavoriteBookByIdOrThrow(favoriteBookId);

        if (!favoriteBook.isWrittenBy(member)) {
            throw UpdateNotAuthorizedException.EXCEPTION;
        }

        if (updateRequest.bookId() == null) {
            throw FavoriteBookRequiredBookException.EXCEPTION;
        }

        Book book = bookService.findBookByIdOrThrow(updateRequest.bookId());

        favoriteBook.changeBook(book);
        favoriteBook.updateTextFields(updateRequest.briefReview(), updateRequest.fullReview());
    }
}
