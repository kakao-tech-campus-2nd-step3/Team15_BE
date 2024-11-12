package kakao.rebit.challenge.fixture;

import kakao.rebit.challenge.dto.ChallengeVerificationRequest;
import kakao.rebit.challenge.entity.ChallengeVerification;
import kakao.rebit.challenge.fixture.values.ChallengeVerificationDefaultValues;

public class ChallengeVerificationFixture {

    public static ChallengeVerification createDefault() {
        ChallengeVerificationDefaultValues defaults = ChallengeVerificationDefaultValues.INSTANCE;
        return new ChallengeVerification(
                defaults.title(),
                defaults.imageKey(),
                defaults.content(),
                defaults.challengeParticipation()
        );
    }

    public static ChallengeVerificationRequest createRequest() {
        ChallengeVerificationDefaultValues defaults = ChallengeVerificationDefaultValues.INSTANCE;
        return new ChallengeVerificationRequest(
                defaults.title(),
                defaults.imageKey(),
                defaults.content()
        );
    }
}
