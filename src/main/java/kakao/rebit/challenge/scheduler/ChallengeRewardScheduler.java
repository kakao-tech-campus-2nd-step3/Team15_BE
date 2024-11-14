package kakao.rebit.challenge.scheduler;

import java.time.LocalDateTime;
import java.util.List;
import kakao.rebit.challenge.entity.Challenge;
import kakao.rebit.challenge.repository.ChallengeRepository;
import kakao.rebit.challenge.service.ChallengeRewardService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ChallengeRewardScheduler {

    private final ChallengeRepository challengeRepository;
    private final ChallengeRewardService challengeRewardService;

    public ChallengeRewardScheduler(ChallengeRepository challengeRepository, ChallengeRewardService challengeRewardService) {
        this.challengeRepository = challengeRepository;
        this.challengeRewardService = challengeRewardService;
    }

    // 10초마다 실행
    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void distributeReward() {
        LocalDateTime now = LocalDateTime.now();

        // 아직 보상이 지급되지 않은 완료된 챌린지 찾기
        List<Challenge> completedChallenges =
                challengeRepository.findAllByIsRewardDistributedFalseAndChallengePeriod_EndDateBefore(now);

        completedChallenges.forEach(challenge -> {
            challengeRewardService.distributeReward(challenge.getId());
            challenge.markAsRewardDistributed();
        });
    }
}
