package kakao.rebit.book.dto;

import java.time.LocalDateTime;

public record BookDetailResponse(
        Long id,
        String isbn,
        String title,
        String author,
        String cover,
        String description,
        String publisher,
        String pubDate,
        String topFullReview,
        String link,
        String briefReview,
        String briefReviewAuthor,
        String briefReviewAuthorImage,
        LocalDateTime createdAt
) {

}
