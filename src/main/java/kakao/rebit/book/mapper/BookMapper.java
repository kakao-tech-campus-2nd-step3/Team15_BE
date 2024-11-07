package kakao.rebit.book.mapper;

import java.time.LocalDateTime;
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
                book.getPubDate(),
                book.getLink()
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
                response.pubDate(),
                response.link()
        );
    }


    public static BookDetailResponse toBookDetailResponse(Book book, FavoriteBook topFavoriteBook) {
        String briefReview =
                topFavoriteBook != null ? topFavoriteBook.getBriefReview() : "한줄평이 없습니다.";
        String topFullReview =
                topFavoriteBook != null ? topFavoriteBook.getFullReview() : "서평이 없습니다.";
        String briefReviewAuthor =
                topFavoriteBook != null ? topFavoriteBook.getMember().getNickname() : "작성자 정보 없음";
        String briefReviewAuthorImage =
                topFavoriteBook != null ? topFavoriteBook.getMember().getImageKey() : null;
        LocalDateTime createdAt =
                topFavoriteBook != null ? topFavoriteBook.getCreatedAt() : null;

        return new BookDetailResponse(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getCover(),
                book.getDescription(),
                book.getPublisher(),
                book.getPubDate(),
                topFullReview,
                book.getLink(),
                briefReview,
                briefReviewAuthor,
                briefReviewAuthorImage,
                createdAt
                );
    }
}
