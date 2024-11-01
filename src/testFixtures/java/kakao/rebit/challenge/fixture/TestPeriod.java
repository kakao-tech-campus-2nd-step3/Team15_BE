package kakao.rebit.challenge.fixture;

import java.time.LocalDateTime;
import kakao.rebit.challenge.entity.Period;

public record TestPeriod(
        Period recruitmentPeriod,
        Period challengePeriod
) {

    private static final long DEFAULT_RECRUITMENT_DAYS = 7;
    private static final long DEFAULT_CHALLENGE_DAYS = 14;

    // 모집 중인 챌린지
    public static TestPeriod recruiting() {
        LocalDateTime now = LocalDateTime.now();
        return new TestPeriod(
                new Period( // 모집 기간: 현재 시간 하루 전부터 7일 후까지
                        now.minusDays(1),
                        now.plusDays(DEFAULT_RECRUITMENT_DAYS)
                ),
                new Period( // 챌린지 기간: 모집 기간 끝난 다음날부터 14일 후까지
                        now.plusDays(DEFAULT_RECRUITMENT_DAYS + 1),
                        now.plusDays(DEFAULT_RECRUITMENT_DAYS + 1 + DEFAULT_CHALLENGE_DAYS)
                )
        );
    }

    // 진행 중인 챌린지
    public static TestPeriod ongoing() {
        LocalDateTime now = LocalDateTime.now();
        return new TestPeriod(
                new Period( // 모집 기간: 7일 전부터 1일 전까지 -> 모집 마감된 상태
                        now.minusDays(DEFAULT_RECRUITMENT_DAYS - 1),
                        now.minusDays(1)
                ),
                new Period( // 챌린지 기간: 모집 마감 다음날부터 14일 후까지 -> 챌린지 진행 중
                        now.plusDays(2),
                        now.plusDays(DEFAULT_RECRUITMENT_DAYS + 1 + DEFAULT_CHALLENGE_DAYS)
                )
        );
    }

    // 모집 마감된 챌린지
    public static TestPeriod recruitmentEnded() {
        return ongoing();   // 모집 마감된 챌린지는 진행 중인 챌린지를 그대로 사용
    }

    // 챌린지 완료된 챌린지
    public static TestPeriod completed() {
        LocalDateTime now = LocalDateTime.now();
        return new TestPeriod(
                new Period( // 모집 기간: 한참 전
                        now.minusDays(DEFAULT_RECRUITMENT_DAYS + DEFAULT_CHALLENGE_DAYS),
                        now.minusDays(DEFAULT_CHALLENGE_DAYS)
                ),
                new Period( // 챌린지 기간: 14일 전부터 1일 전까지 -> 챌린지 완료된 상태
                        now.minusDays(DEFAULT_CHALLENGE_DAYS + 1),
                        now.minusDays(1)
                )
        );
    }
}
