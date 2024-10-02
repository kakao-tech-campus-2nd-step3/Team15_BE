package kakao.rebit.challenge.repository;

import kakao.rebit.challenge.entity.Challenge;
import kakao.rebit.challenge.entity.ChallengeParticipation;
import kakao.rebit.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChallengeParticipationRepository extends JpaRepository<ChallengeParticipation, Long> {

    @Query("SELECT cp FROM ChallengeParticipation cp JOIN FETCH cp.member JOIN FETCH cp.challenge WHERE cp.challenge = :challenge")
    Page<ChallengeParticipation> findAllByChallenge(@Param("challenge") Challenge challenge, Pageable pageable);

    Boolean existsByMemberAndChallenge(Member member, Challenge challenge);
}
