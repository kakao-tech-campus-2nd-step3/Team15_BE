package kakao.rebit.book.mapper;

import kakao.rebit.book.dto.AladinApiResponseResponse;
import kakao.rebit.book.dto.BookResponse;
import kakao.rebit.book.entity.Book;

public class BookMapper {

    public static BookResponse toBookResponse(Book book) {
        return new BookResponse(
            book.getId(),
            book.getIsbn(),
            book.getTitle(),
            book.getAuthor(),
            book.getCover(),
            book.getDescription(),
            book.getPublisher(),
            book.getPubDate()
        );
    }

    public static Book toBookEntity(AladinApiResponseResponse response) {
        return new Book(
            response.isbn(),
            response.title(),
            response.description(),
            response.author(),
            response.publisher(),
            response.cover(),
            response.pubDate()
        );
    }
}
