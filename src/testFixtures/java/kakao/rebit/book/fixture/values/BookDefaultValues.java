package kakao.rebit.book.fixture.values;

public record BookDefaultValues(

        String isbn,
        String title,
        String description,
        String author,
        String publisher,
        String cover,
        String pubDate,
        String link
) {

    public static final BookDefaultValues INSTANCE = new BookDefaultValues(
            "테스트 isbn",
            "테스트용 제목",
            "테스트용 설명",
            "테스트용 작가",
            "테스트용 출판사",
            "default-cover-image",
            "2024-11-06",
            "https://default-link.com"
    );
}
