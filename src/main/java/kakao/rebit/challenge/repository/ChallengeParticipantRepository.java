package kakao.rebit.challenge.repository;

import kakao.rebit.challenge.entity.Challenge;
import kakao.rebit.challenge.entity.ChallengeParticipant;
import kakao.rebit.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChallengeParticipantRepository extends JpaRepository<ChallengeParticipant, Long> {

    @Query("SELECT cp FROM ChallengeParticipant cp JOIN FETCH cp.member JOIN FETCH cp.challenge WHERE cp.challenge = :challenge")
    Page<ChallengeParticipant> findAllByChallenge(@Param("challenge") Challenge challenge, Pageable pageable);

    Boolean existsByMemberAndChallenge(Member member, Challenge challenge);
}
