package kakao.rebit.feed.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "isbn", "title", "author", "cover", "description", "publisher", "pubDate" })
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
