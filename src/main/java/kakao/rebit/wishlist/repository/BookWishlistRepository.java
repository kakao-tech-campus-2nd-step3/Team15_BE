package kakao.rebit.wishlist.repository;

import kakao.rebit.wishlist.entity.BookWishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookWishlistRepository extends JpaRepository<BookWishlist, Long> {

    Page<BookWishlist> findByMemberId(Long memberId, Pageable pageable);
}
