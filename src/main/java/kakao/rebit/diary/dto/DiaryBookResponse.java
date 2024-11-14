package kakao.rebit.diary.dto;

public record DiaryBookResponse(
        Long id,
        String isbn,
        String title,
        String description,
        String author,
        String publisher,
        String cover,
        String pubDate,
        String link
) {

}
