package kakao.rebit.feed.dto.response;

public record BookResponse(
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
