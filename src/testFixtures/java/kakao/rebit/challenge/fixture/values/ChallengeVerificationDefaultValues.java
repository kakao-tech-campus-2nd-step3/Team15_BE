package kakao.rebit.challenge.fixture.values;

import kakao.rebit.challenge.entity.ChallengeParticipation;
import kakao.rebit.challenge.fixture.ChallengeFixture;
import kakao.rebit.challenge.fixture.ChallengeParticipationFixture;
import kakao.rebit.member.fixture.MemberFixture;

public record ChallengeVerificationDefaultValues(
        String title,
        String imageKey,
        String content,
        ChallengeParticipation challengeParticipation
) {

    public static final ChallengeVerificationDefaultValues INSTANCE = new ChallengeVerificationDefaultValues(
            "테스트용 챌린지 검증 제목",
            "default-image-key",
            "테스트용 챌린지 검증 내용",
            ChallengeParticipationFixture.createDefault(
                    ChallengeFixture.createDefault(MemberFixture.createDefault()),
                    MemberFixture.createDefault(),
                    ChallengeFixture.createDefault(MemberFixture.createDefault()).getMinimumEntryFee()
            )
    );
}
