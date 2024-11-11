package kakao.rebit.wishlist.service;

import kakao.rebit.book.entity.Book;
import kakao.rebit.book.service.BookService;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.service.MemberService;
import kakao.rebit.wishlist.dto.response.BookWishlistResponse;
import kakao.rebit.wishlist.entity.BookWishlist;
import kakao.rebit.wishlist.exception.AlreadyInWishlistException;
import kakao.rebit.wishlist.repository.BookWishlistRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookWishlistService {

    private final BookWishlistRepository bookWishlistRepository;
    private final MemberService memberService;
    private final BookService bookService;

    public BookWishlistService(BookWishlistRepository bookWishlistRepository,
            MemberService memberService,
            BookService bookService) {
        this.bookWishlistRepository = bookWishlistRepository;
        this.memberService = memberService;
        this.bookService = bookService;
    }

    @Transactional(readOnly = true)
    public Page<BookWishlistResponse> getBookWishlist(Long memberId, Pageable pageable) {
        return bookWishlistRepository.findByMemberId(memberId, pageable)
                .map(bookWishlist -> new BookWishlistResponse(
                        bookWishlist.getBook().getIsbn(),
                        true
                ));
    }

    @Transactional
    public void addBookWishlist(Long memberId, String isbn) {
        Member member = memberService.findMemberByIdOrThrow(memberId);
        Book book = bookService.findByIsbnOrThrow(isbn);

        // 이미 위시리스트에 존재하는 경우 예외 던지기
        if (bookWishlistRepository.existsByMemberAndBook(member, book)) {
            throw AlreadyInWishlistException.EXCEPTION;
        }

        bookWishlistRepository.save(new BookWishlist(member, book));
    }

    @Transactional
    public void deleteBookWishlist(Long memberId, String isbn) {
        Member member = memberService.findMemberByIdOrThrow(memberId);
        Book book = bookService.findByIsbnOrThrow(isbn);

        BookWishlist bookWishlist = bookWishlistRepository.findByMemberAndBookOrThrow(member, book);

        bookWishlistRepository.delete(bookWishlist);
    }
}
