package kakao.rebit.challenge.dto;

import java.time.LocalDateTime;

public record ChallengeParticipationMemberResponse(
        Long participationId,
        Long memberId,
        String nickname,
        String imageUrl,
        LocalDateTime participatedAt,
        Integer entryFee
) {

}
