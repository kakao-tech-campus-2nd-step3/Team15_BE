package kakao.rebit.challenge.fixture;

import kakao.rebit.challenge.dto.ChallengeRequest;
import kakao.rebit.challenge.entity.Challenge;
import kakao.rebit.challenge.fixture.values.ChallengeDefaultValues;
import kakao.rebit.member.entity.Member;

public class ChallengeFixture {

    public static ChallengeRequest createRequest() {
        return new ChallengeRequest(
                ChallengeDefaultValues.INSTANCE.title(),
                ChallengeDefaultValues.INSTANCE.content(),
                ChallengeDefaultValues.INSTANCE.imageKey(),
                ChallengeDefaultValues.INSTANCE.challengeType(),
                ChallengeDefaultValues.INSTANCE.minimumEntryFee(),
                ChallengeDefaultValues.INSTANCE.recruitmentStartDate(),
                ChallengeDefaultValues.INSTANCE.recruitmentEndDate(),
                ChallengeDefaultValues.INSTANCE.challengeStartDate(),
                ChallengeDefaultValues.INSTANCE.challengeEndDate(),
                ChallengeDefaultValues.INSTANCE.minHeadcount(),
                ChallengeDefaultValues.INSTANCE.maxHeadcount()
        );
    }

    public static Challenge createDefault(Member host) {
        return createWithPeriod(host, TestPeriod.recruiting());
    }

    public static Challenge createRecruiting(Member host) {
        return createWithPeriod(host, TestPeriod.recruiting());
    }

    public static Challenge createRecruitmentEnded(Member host) {
        return createWithPeriod(host, TestPeriod.recruitmentEnded());
    }

    public static Challenge createOngoing(Member host) {
        return createWithPeriod(host, TestPeriod.ongoing());
    }

    public static Challenge createCompleted(Member host) {
        return createWithPeriod(host, TestPeriod.completed());
    }

    private static Challenge createWithPeriod(Member host, TestPeriod period) {
        ChallengeDefaultValues defaults = ChallengeDefaultValues.INSTANCE;
        return new Challenge(
                host,
                defaults.title(),
                defaults.content(),
                defaults.imageKey(),
                defaults.challengeType(),
                defaults.minimumEntryFee(),
                period.recruitmentPeriod(),
                period.challengePeriod(),
                defaults.headcountLimit()
        );
    }
}
