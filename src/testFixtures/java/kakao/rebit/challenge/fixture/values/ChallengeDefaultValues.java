package kakao.rebit.challenge.fixture.values;

import kakao.rebit.challenge.entity.ChallengeType;
import kakao.rebit.challenge.entity.HeadcountLimit;

public record ChallengeDefaultValues(
        String title,
        String content,
        String imageKey,
        ChallengeType challengeType,
        int minimumEntryFee,
        HeadcountLimit headcountLimit
) {

    public static final ChallengeDefaultValues INSTANCE = new ChallengeDefaultValues(
            "테스트용 챌린지 제목",
            "테스트용 챌린지 내용",
            "default-image-key",
            ChallengeType.DAILY_WRITING,
            1000,
            new HeadcountLimit(1, 10)
    );
}
