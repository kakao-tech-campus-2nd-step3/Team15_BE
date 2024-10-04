package kakao.rebit.feed.dto.response;

public record FeedBookResponse(
    Long id,
    String isbn,
    String title,
    String author,
    String cover,
    String description,
    String publisher,
    String pubDate
) {

}
