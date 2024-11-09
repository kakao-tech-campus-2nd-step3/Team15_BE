package kakao.rebit.book.mapper;

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
                book.getDescription(),
                book.getAuthor(),
                book.getPublisher(),
                book.getCover(),
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
                book.getDescription(),
                book.getAuthor(),
                book.getPublisher(),
                book.getCover(),
                book.getPubDate(),
                book.getLink(),
                topFavoriteBook != null ? topFavoriteBook.getFullReview() : "등록된 서평이 없습니다.",
                topFavoriteBook != null ? topFavoriteBook.getBriefReview() : "등록된 한줄평이 없습니다.",
                topFavoriteBook != null ? topFavoriteBook.getMember().getNickname() : "등록된 이름이 없습니다.",
                topFavoriteBook != null ? topFavoriteBook.getMember().getImageKey() : "등록된 이미지가 없습니다.",
                topFavoriteBook != null ? topFavoriteBook.getCreatedAt(): null

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
