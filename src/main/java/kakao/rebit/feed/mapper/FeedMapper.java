package kakao.rebit.feed.mapper;

import kakao.rebit.book.entity.Book;
import kakao.rebit.feed.dto.request.create.CreateFavoriteBookRequest;
import kakao.rebit.feed.dto.request.create.CreateFeedRequest;
import kakao.rebit.feed.dto.request.create.CreateMagazineRequest;
import kakao.rebit.feed.dto.request.create.CreateStoryRequest;
import kakao.rebit.feed.dto.response.FavoriteBookResponse;
import kakao.rebit.feed.dto.response.FeedAuthorResponse;
import kakao.rebit.feed.dto.response.FeedBookResponse;
import kakao.rebit.feed.dto.response.FeedResponse;
import kakao.rebit.feed.dto.response.MagazineResponse;
import kakao.rebit.feed.dto.response.StoryResponse;
import kakao.rebit.feed.entity.FavoriteBook;
import kakao.rebit.feed.entity.Feed;
import kakao.rebit.feed.entity.Magazine;
import kakao.rebit.feed.entity.Story;
import kakao.rebit.feed.exception.feed.InvalidFeedFormatException;
import kakao.rebit.feed.service.LikesService;
import kakao.rebit.member.entity.Member;
import kakao.rebit.s3.service.S3Service;
import org.springframework.stereotype.Component;

@Component
public class FeedMapper {

    private final S3Service s3Service;
    private final LikesService likesService;

    public FeedMapper(S3Service s3Service, LikesService likesService) {
        this.s3Service = s3Service;
        this.likesService = likesService;
    }

    /**
     * Entity -> DTO(Response) 변환
     */
    public FeedResponse toFeedResponse(Member viewer, Feed feed) {
        return switch (feed) {
            case FavoriteBook favoriteBook -> toFavoriteBookResponse(viewer, favoriteBook);
            case Magazine magazine -> toMagazineResponse(viewer, magazine);
            case Story story -> toStoryResponse(viewer, story);
            default -> throw InvalidFeedFormatException.EXCEPTION;
        };
    }

    /**
     * DTO(CreateRequest) -> Entity 변환
     */
    public Feed toFeed(Member author, Book book, CreateFeedRequest feedRequest) {
        return switch (feedRequest) {
            case CreateFavoriteBookRequest favoriteBookRequest -> toFavoriteBook(author, book, favoriteBookRequest);
            case CreateMagazineRequest magazineRequest -> toMagazine(author, book, magazineRequest);
            case CreateStoryRequest storyRequest -> toStory(author, book, storyRequest);
            default -> throw InvalidFeedFormatException.EXCEPTION;
        };
    }

    private FavoriteBookResponse toFavoriteBookResponse(Member viewer, FavoriteBook favoriteBook) {
        return new FavoriteBookResponse(
                favoriteBook.getId(),
                this.toAuthorResponse(favoriteBook.getMember()),
                this.toBookResponse(favoriteBook.getBook()),
                favoriteBook.getType(),
                favoriteBook.getLikes(),
                likesService.isLiked(viewer, favoriteBook),
                favoriteBook.getBriefReview(),
                favoriteBook.getFullReview()
        );
    }

    private MagazineResponse toMagazineResponse(Member viewer, Magazine magazine) {
        return new MagazineResponse(
                magazine.getId(),
                this.toAuthorResponse(magazine.getMember()),
                this.toBookResponse(magazine.getBook()),
                magazine.getType(),
                magazine.getLikes(),
                likesService.isLiked(viewer, magazine),
                magazine.getName(),
                magazine.getImageKey(),
                s3Service.getDownloadUrl(magazine.getImageKey()).presignedUrl(),
                magazine.getContent()
        );
    }

    private StoryResponse toStoryResponse(Member viewer, Story story) {
        return new StoryResponse(
                story.getId(),
                this.toAuthorResponse(story.getMember()),
                this.toBookResponse(story.getBook()),
                story.getType(),
                story.getLikes(),
                likesService.isLiked(viewer, story),
                story.getImageKey(),
                s3Service.getDownloadUrl(story.getImageKey()).presignedUrl(),
                story.getContent()
        );
    }

    private FeedAuthorResponse toAuthorResponse(Member author) {
        return new FeedAuthorResponse(
                author.getId(),
                author.getNickname(),
                author.getImageKey(),
                s3Service.getDownloadUrl(author.getImageKey()).presignedUrl()
        );
    }

    private FeedBookResponse toBookResponse(Book book) {
        if (book == null) {
            return null;
        }
        return new FeedBookResponse(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getDescription(),
                book.getAuthor(),
                book.getPublisher(),
                book.getCover(),
                book.getPubDate()
        );
    }

    private FavoriteBook toFavoriteBook(Member author, Book book,
            CreateFavoriteBookRequest request) {
        return new FavoriteBook(
                author,
                book,
                request.getBriefReview(),
                request.getFullReview()
        );
    }

    private Magazine toMagazine(Member author, Book book, CreateMagazineRequest request) {
        return new Magazine(
                author,
                book,
                request.getName(),
                request.getImageKey(),
                request.getContent()
        );
    }

    private Story toStory(Member author, Book book, CreateStoryRequest request) {
        return new Story(
                author,
                book,
                request.getImageKey(),
                request.getContent()
        );
    }
}
