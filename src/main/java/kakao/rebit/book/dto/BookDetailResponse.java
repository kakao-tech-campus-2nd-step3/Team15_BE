package kakao.rebit.book.dto;

public record BookDetailResponse(
    Long id,
    String isbn,
    String title,
    String author,
    String cover,
    String description,
    String publisher,
    String pubDate,
    String briefReview,
    String topFullReview
) {

}
