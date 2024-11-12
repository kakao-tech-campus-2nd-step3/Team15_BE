package kakao.rebit.book.mapper;

import java.time.LocalDateTime;
import kakao.rebit.book.dto.AladinApiResponseResponse;
import kakao.rebit.book.dto.BookDetailResponse;
import kakao.rebit.book.dto.BookResponse;
import kakao.rebit.book.dto.BriefReviewResponse;
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
        return new BookDetailResponse(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getCover(),
                book.getDescription(),
                book.getPublisher(),
                book.getPubDate(),
                book.getLink(),
                topFavoriteBook.getFullReview(),
                topFavoriteBook.getBriefReview(),
                topFavoriteBook.getMember().getNickname(),
                topFavoriteBook.getMember().getImageKey(),
                topFavoriteBook.getCreatedAt()
        );
    }

    public static BriefReviewResponse toBriefReviewResponse(FavoriteBook favoriteBook) {
        return new BriefReviewResponse(
                favoriteBook.getBriefReview(),
                favoriteBook.getMember().getNickname(),
                favoriteBook.getMember().getImageKey(),
                favoriteBook.getCreatedAt()
        );
    }
}
