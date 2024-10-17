package kakao.rebit.challenge.dto;

import java.time.LocalDateTime;

public record ChallengeParticipationMemberResponse(
        Long participationId,
        Long memberId,
        String nickname,
        String imageKey,
        String presignedUrl,
        LocalDateTime participatedAt,
        Integer entryFee
) {

}
