package kakao.rebit.wishlist.repository;

import java.util.Optional;
import kakao.rebit.challenge.entity.Challenge;
import kakao.rebit.wishlist.entity.ChallengeWishlist;
import kakao.rebit.member.entity.Member;
import kakao.rebit.wishlist.exception.ChallengeWishlistNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeWishlistRepository extends JpaRepository<ChallengeWishlist, Long> {

    Page<ChallengeWishlist> findByMemberId(Long memberId, Pageable pageable);

    boolean existsByMemberAndChallenge(Member member, Challenge challenge);

    Optional<ChallengeWishlist> findByMemberAndChallenge(Member member, Challenge challenge);

    default ChallengeWishlist findByMemberAndChallengeOrThrow(Member member, Challenge challenge) {
        return findByMemberAndChallenge(member, challenge)
                .orElseThrow(() -> ChallengeWishlistNotFoundException.EXCEPTION);
    }
}
