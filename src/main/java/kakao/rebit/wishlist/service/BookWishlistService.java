package kakao.rebit.wishlist.service;

import kakao.rebit.book.entity.Book;
import kakao.rebit.book.repository.BookRepository;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.repository.MemberRepository;
import kakao.rebit.wishlist.entity.BookWishlist;
import kakao.rebit.wishlist.repository.BookWishlistRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class BookWishlistService {

    private final BookWishlistRepository bookWishlistRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;

    public BookWishlistService(BookWishlistRepository bookWishlistRepository,
        MemberRepository memberRepository, BookRepository bookRepository) {
        this.bookWishlistRepository = bookWishlistRepository;
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public Page<String> getBookWishlist(Long memberId, Pageable pageable) {
        return bookWishlistRepository.findByMemberId(memberId, pageable)
            .map(bookWishlist -> bookWishlist.getBook().getIsbn());
    }

    @Transactional
    public void addBookWishlist(Long memberId, String isbn) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        Book book = bookRepository.findByIsbn(isbn)
            .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        bookWishlistRepository.save(new BookWishlist(member, book));
    }

    @Transactional
    public void deleteBookWishlist(Long memberId, String isbn) {
        bookWishlistRepository.findAll().stream()
            .filter(bookWishlist -> bookWishlist.getMember().getId().equals(memberId)
                && bookWishlist.getBook().getIsbn().equals(isbn))
            .findFirst()
            .ifPresent(bookWishlistRepository::delete);
    }
}
