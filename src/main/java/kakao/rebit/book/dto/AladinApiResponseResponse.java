package kakao.rebit.book.dto;

public record AladinApiResponseResponse(String isbn, String title, String description, String author,
                                        String publisher, String cover, String pubDate) {
}
