package kakao.rebit.wishlist.repository;

import java.util.Optional;
import kakao.rebit.book.entity.Book;
import kakao.rebit.member.entity.Member;
import kakao.rebit.wishlist.entity.BookWishlist;
import kakao.rebit.wishlist.exception.BookWishlistNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookWishlistRepository extends JpaRepository<BookWishlist, Long> {

    Page<BookWishlist> findByMemberId(Long memberId, Pageable pageable);
    Optional<BookWishlist> findByMemberAndBook(Member member, Book book);
    default BookWishlist findByMemberAndBookOrThrow(Member member, Book book) {
        return findByMemberAndBook(member, book)
                .orElseThrow(() -> BookWishlistNotFoundException.EXCEPTION);
    }
}
