package kakao.rebit.challenge.fixture.values;

import java.time.LocalDateTime;
import java.util.UUID;
import kakao.rebit.challenge.entity.ChallengeType;
import kakao.rebit.challenge.entity.HeadcountLimit;

public record ChallengeDefaultValues(
        String title,
        String content,
        String imageKey,
        ChallengeType challengeType,
        LocalDateTime recruitmentStartDate,
        LocalDateTime recruitmentEndDate,
        LocalDateTime challengeStartDate,
        LocalDateTime challengeEndDate,
        int minHeadcount,
        int maxHeadcount,
        int minimumEntryFee,
        HeadcountLimit headcountLimit
) {

    public static final ChallengeDefaultValues INSTANCE = new ChallengeDefaultValues(
            "테스트용 챌린지 제목",
            "테스트용 챌린지 내용",
            "challenge/" + UUID.randomUUID() + "/filename",
            ChallengeType.DAILY_WRITING,
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2),
            LocalDateTime.now().plusDays(3),
            LocalDateTime.now().plusDays(5),
            1,
            10,
            1000,
            new HeadcountLimit(1, 10)
    );
}
