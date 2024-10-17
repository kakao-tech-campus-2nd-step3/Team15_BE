package kakao.rebit.challenge.dto;

import java.time.LocalDateTime;

public record ChallengeVerificationResponse(
        Long id,
        Long participationId,
        AuthorResponse author,
        String title,
        String imageKey,
        String presignedUrl,
        String content,
        LocalDateTime createdAt
) {

}
