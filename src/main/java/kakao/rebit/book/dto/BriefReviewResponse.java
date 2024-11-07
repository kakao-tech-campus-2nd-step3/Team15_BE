package kakao.rebit.book.dto;

import java.time.LocalDateTime;

public record BriefReviewResponse(
        String briefReview,
        String briefReviewAuthor,
        String briefReviewAuthorImage,
        LocalDateTime createdAt
) {

}
