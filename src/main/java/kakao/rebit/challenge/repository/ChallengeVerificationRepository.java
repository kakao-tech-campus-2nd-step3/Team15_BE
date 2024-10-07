package kakao.rebit.challenge.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import kakao.rebit.challenge.entity.Challenge;
import kakao.rebit.challenge.entity.ChallengeVerification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChallengeVerificationRepository extends JpaRepository<ChallengeVerification, Long> {

    @EntityGraph(attributePaths = {"challengeParticipation", "challengeParticipation.member", "challengeParticipation.challenge"})
    @Query("SELECT cv FROM ChallengeVerification cv WHERE cv.challengeParticipation.challenge = :challenge")
    Page<ChallengeVerification> findAllByChallengeWithParticipants(@Param("challenge") Challenge challenge, Pageable pageable);

    @EntityGraph(attributePaths = {"challengeParticipation", "challengeParticipation.member"})
    Optional<ChallengeVerification> findByIdAndChallengeParticipation_Challenge(Long verificationId, Challenge challenge);

    @Query("SELECT EXISTS (SELECT 1 FROM ChallengeVerification cv " +
            "WHERE cv.challengeParticipation.id = :participationId " +
            "AND FUNCTION('DATE', cv.createdAt) = FUNCTION('DATE', :now))")
    boolean existsDailyVerification(@Param("participationId") Long participationId, @Param("now") LocalDateTime now);
}
