package kakao.rebit.challenge.dto;

import java.time.LocalDateTime;
import kakao.rebit.challenge.entity.ChallengeType;

public record ChallengeRequest(
        String title,
        String content,
        String imageUrl,
        ChallengeType type,
        Integer minimumEntryFee,
        LocalDateTime recruitmentStartDate,
        LocalDateTime recruitmentEndDate,
        LocalDateTime challengeStartDate,
        LocalDateTime challengeEndDate,
        Integer minHeadcount,
        Integer maxHeadcount
) {

}
