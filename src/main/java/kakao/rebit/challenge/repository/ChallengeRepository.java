package kakao.rebit.challenge.repository;

import java.time.LocalDateTime;
import java.util.List;
import kakao.rebit.challenge.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    List<Challenge> findAllByIsRewardDistributedFalseAndChallengePeriod_EndDateBefore(LocalDateTime endDate);
}
