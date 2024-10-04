package kakao.rebit.challenge.repository;

import java.util.Optional;
import kakao.rebit.challenge.entity.Challenge;
import kakao.rebit.challenge.entity.ChallengeParticipation;
import kakao.rebit.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChallengeParticipationRepository extends JpaRepository<ChallengeParticipation, Long> {

    @EntityGraph(attributePaths = {"member"})
    @Query("SELECT cp FROM ChallengeParticipation cp WHERE cp.challenge = :challenge")
    Page<ChallengeParticipation> findAllByChallengeWithMember(@Param("challenge") Challenge challenge, Pageable pageable);

    Boolean existsByMemberAndChallenge(Member member, Challenge challenge);

    @EntityGraph(attributePaths = {"member", "challenge"})
    Optional<ChallengeParticipation> findByMemberAndChallenge(Member member, Challenge challenge);
}
