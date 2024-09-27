package kakao.rebit.feed.dto.response;

public record BookResponse(
        Long id,
        String isbn,
        String title,
        String description,
        String author,
        String publisher,
        String imageUrl
) {

}
