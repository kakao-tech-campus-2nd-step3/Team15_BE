package kakao.rebit.challenge.repository;

import java.util.Optional;
import kakao.rebit.challenge.entity.Challenge;
import kakao.rebit.challenge.entity.ChallengeParticipation;
import kakao.rebit.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeParticipationRepository extends JpaRepository<ChallengeParticipation, Long> {

    @EntityGraph(attributePaths = {"member"})
    Page<ChallengeParticipation> findAllByChallenge(Challenge challenge, Pageable pageable);

    Boolean existsByChallengeAndMember(Challenge challenge, Member member);

    @EntityGraph(attributePaths = {"member", "challenge"})
    Optional<ChallengeParticipation> findByMemberAndChallenge(Member member, Challenge challenge);

    @EntityGraph(attributePaths = {"challenge", "challenge.member"})
    Page<ChallengeParticipation> findAllByMember(Member member, Pageable pageable);
}
