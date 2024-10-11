package kakao.rebit.wishlist.repository;

import kakao.rebit.wishlist.entity.ChallengeWishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeWishlistRepository extends JpaRepository<ChallengeWishlist, Long> {

    Page<ChallengeWishlist> findByMemberId(Long memberId, Pageable pageable);
}
