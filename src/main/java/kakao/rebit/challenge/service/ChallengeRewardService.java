package kakao.rebit.challenge.service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kakao.rebit.challenge.entity.Challenge;
import kakao.rebit.challenge.entity.ChallengeParticipation;
import kakao.rebit.challenge.entity.Period;
import kakao.rebit.challenge.repository.ChallengeParticipationRepository;
import kakao.rebit.challenge.repository.ChallengeVerificationRepository;
import kakao.rebit.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChallengeRewardService {

    private final ChallengeService challengeService;
    private final ChallengeParticipationRepository challengeParticipationRepository;
    private final ChallengeVerificationRepository challengeVerificationRepository;

    public ChallengeRewardService(
            ChallengeService challengeService,
            ChallengeParticipationRepository challengeParticipationRepository,
            ChallengeVerificationRepository challengeVerificationRepository
    ) {
        this.challengeService = challengeService;
        this.challengeParticipationRepository = challengeParticipationRepository;
        this.challengeVerificationRepository = challengeVerificationRepository;
    }

    @Transactional
    public void distributeReward(Long challengeId) {
        Challenge challenge = challengeService.findChallengeByIdOrThrow(challengeId);

        // 챌린지 기간의 총 일수 계산
        long totalDays = getTotalDays(challenge);

        List<ChallengeParticipation> participations = challengeParticipationRepository.findAllByChallenge(challenge);

        if (participations.isEmpty()) {    // 참여자가 없으면 종료
            return;
        }

        // 챌린지 참여자들의 인증 횟수 계산
        Map<Member, Long> verificationCounts = getVerificationCounts(participations);

        // 매일 인증을 완료한 참여자들 찾기
        List<Member> winners = getWinners(verificationCounts, totalDays);

        if (winners.isEmpty()) {           // 챌린지 성공한 참여자가 없으면 종료
            return;
        }

        int totalReward = challenge.getTotalEntryFee();

        // 챌린지 성공한 참여자들에게 분배할 상금 계산
        int rewardPerWinner = totalReward / winners.size();

        // 챌린지 성공한 참여자들에게 상금 지급
        winners.forEach(winner -> winner.addPoints(rewardPerWinner));
    }

    private long getTotalDays(Challenge challenge) {
        Period challengePeriod = challenge.getChallengePeriod();
        return ChronoUnit.DAYS.between(
                challengePeriod.getStartDate().toLocalDate(),
                challengePeriod.getEndDate().toLocalDate()
        ) + 1;  // 시작일 포함 (+1)
    }

    private Map<Member, Long> getVerificationCounts(List<ChallengeParticipation> participations) {
        return participations.stream()
                .collect(Collectors.toMap(
                        ChallengeParticipation::getMember,
                        challengeVerificationRepository::countByChallengeParticipation
                ));
    }

    private List<Member> getWinners(Map<Member, Long> verificationCounts, long totalDays) {
        return verificationCounts.entrySet().stream()
                .filter(entry -> entry.getValue() == totalDays)
                .map(Map.Entry::getKey)
                .toList();
    }
}
