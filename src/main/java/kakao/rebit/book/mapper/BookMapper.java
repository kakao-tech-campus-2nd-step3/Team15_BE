package kakao.rebit.book.mapper;

import kakao.rebit.book.dto.AladinApiResponseResponse;
import kakao.rebit.book.dto.BookDetailResponse;
import kakao.rebit.book.dto.BookResponse;
import kakao.rebit.book.entity.Book;
import kakao.rebit.feed.entity.FavoriteBook;

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

    public static BookDetailResponse toBookDetailResponse(Book book, FavoriteBook topFavoriteBook) {
        String briefReview =
            topFavoriteBook != null ? topFavoriteBook.getBriefReview() : "한줄평이 없습니다.";
        String topFullReview =
            topFavoriteBook != null ? topFavoriteBook.getFullReview() : "서평이 없습니다.";

        return new BookDetailResponse(
            book.getId(),
            book.getIsbn(),
            book.getTitle(),
            book.getAuthor(),
            book.getCover(),
            book.getDescription(),
            book.getPublisher(),
            book.getPubDate(),
            topFullReview
        );
    }
}
