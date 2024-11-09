package kakao.rebit.book.dto;

import java.time.LocalDateTime;

public record BookDetailResponse(
        Long id,
        String isbn,
        String title,
        String description,
        String author,
        String publisher,
        String cover,
        String pubDate,
        String link,
        String topFullReview,
        String briefReview,
        String briefReviewAuthor,
        String briefReviewAuthorImage,
        LocalDateTime createdAt
) {

}
