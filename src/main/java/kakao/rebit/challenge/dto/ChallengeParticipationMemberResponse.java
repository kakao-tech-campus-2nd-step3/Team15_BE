package kakao.rebit.challenge.dto;

import java.time.LocalDateTime;

public record ChallengeParticipationMemberResponse(
        Long id,
        String nickname,
        String imageUrl,
        LocalDateTime participatedAt,
        Integer entryFee
) {

}
