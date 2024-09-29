package kakao.rebit.feed.mapper;

import kakao.rebit.book.entity.Book;
import kakao.rebit.book.repository.BookRepository;
import kakao.rebit.feed.dto.request.create.CreateFavoriteBookRequest;
import kakao.rebit.feed.dto.request.create.CreateFeedRequest;
import kakao.rebit.feed.dto.request.create.CreateMagazineRequest;
import kakao.rebit.feed.dto.request.create.CreateStoryRequest;
import kakao.rebit.feed.dto.request.update.UpdateFavoriteBookRequest;
import kakao.rebit.feed.dto.request.update.UpdateFeedRequest;
import kakao.rebit.feed.dto.request.update.UpdateMagazineRequest;
import kakao.rebit.feed.dto.request.update.UpdateStoryRequest;
import kakao.rebit.feed.dto.response.AuthorResponse;
import kakao.rebit.feed.dto.response.BookResponse;
import kakao.rebit.feed.dto.response.FavoriteBookResponse;
import kakao.rebit.feed.dto.response.FeedResponse;
import kakao.rebit.feed.dto.response.MagazineResponse;
import kakao.rebit.feed.dto.response.StoryResponse;
import kakao.rebit.feed.entity.FavoriteBook;
import kakao.rebit.feed.entity.Feed;
import kakao.rebit.feed.entity.Magazine;
import kakao.rebit.feed.entity.Story;
import kakao.rebit.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class FeedMapper {

    private final BookRepository bookRepository;

    public FeedMapper(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Entity -> DTO(Response) 변환
     */

    public AuthorResponse toAuthorResponse(Member member) {
        return new AuthorResponse(member.getId(), member.getNickname(), member.getImageUrl());
    }

    public BookResponse toBookResponse(Book book) {
        return new BookResponse(book.getId(), book.getIsbn(), book.getTitle(),
                book.getDescription(), book.getAuthor(), book.getPublisher(), book.getImageUrl());
    }

    public FeedResponse toFeedResponse(Feed feed) {
        if (feed instanceof FavoriteBook) {
            return new FavoriteBookResponse(feed.getId(), toAuthorResponse(feed.getMember()),
                    toBookResponse(feed.getBook()), feed.getType(),
                    ((FavoriteBook) feed).getBriefReview(), ((FavoriteBook) feed).getFullReview());
        }
        if (feed instanceof Magazine) {
            return new MagazineResponse(feed.getId(), toAuthorResponse(feed.getMember()),
                    toBookResponse(feed.getBook()), feed.getType(),
                    ((Magazine) feed).getName(), ((Magazine) feed).getImageUrl(),
                    ((Magazine) feed).getContent());
        }
        if (feed instanceof Story) {
            return new StoryResponse(feed.getId(), toAuthorResponse(feed.getMember()),
                    toBookResponse(feed.getBook()), feed.getType(),
                    ((Story) feed).getImageUrl(), ((Story) feed).getContent());
        }
        throw new IllegalStateException("유효하지 않는 피드입니다.");
    }

    public FavoriteBookResponse toFavoriteBookResponse(FavoriteBook favoriteBook) {
        return new FavoriteBookResponse(favoriteBook.getId(),
                this.toAuthorResponse(favoriteBook.getMember()),
                this.toBookResponse(favoriteBook.getBook()),
                favoriteBook.getType(), favoriteBook.getBriefReview(),
                favoriteBook.getFullReview());
    }

    public MagazineResponse toMagazineResponse(Magazine magazine) {
        return new MagazineResponse(
                magazine.getId(),
                this.toAuthorResponse(magazine.getMember()),
                this.toBookResponse(magazine.getBook()),
                magazine.getType(),
                magazine.getName(),
                magazine.getImageUrl(),
                magazine.getContent()
        );
    }

    public StoryResponse toStoryResponse(Story story) {
        return new StoryResponse(
                story.getId(),
                this.toAuthorResponse(story.getMember()),
                this.toBookResponse(story.getBook()),
                story.getType(),
                story.getImageUrl(),
                story.getContent()
        );
    }

    /**
     * DTO(Request) -> Entity 변환
     */

    /**
     * CreateFeedRequest 하위 항목들의 타입을 체크하고 각각의 엔티티로 반환
     */
    public Feed toFeed(Member member, CreateFeedRequest feedRequest) {
        // request에 bookId가 있을 경우만 book을 가져온다.
        Book book = null;
        if (feedRequest.getBookId() != null) {
            book = bookRepository.findById(feedRequest.getBookId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 책이 존재하지 않습니다."));
        }

        // 각 하위 클래스 생성
        if (feedRequest instanceof CreateFavoriteBookRequest) {
            if (book == null) {
                throw new IllegalStateException("인생책 필드는 반드시 책이 존재 해야됩니다.");
            }
            return new FavoriteBook(member, book,
                    ((CreateFavoriteBookRequest) feedRequest).getBriefReview(),
                    ((CreateFavoriteBookRequest) feedRequest).getFullReview());
        }
        if (feedRequest instanceof CreateMagazineRequest) {
            return new Magazine(member, book, ((CreateMagazineRequest) feedRequest).getName(),
                    ((CreateMagazineRequest) feedRequest).getImageUrl(),
                    ((CreateMagazineRequest) feedRequest).getContent());
        }
        if (feedRequest instanceof CreateStoryRequest) {
            return new Story(member, book, ((CreateStoryRequest) feedRequest).getImageUrl(),
                    ((CreateStoryRequest) feedRequest).getContent());
        }
        throw new IllegalStateException("유효하지 않는 피드 요청입니다.");
    }

    /**
     * UpdateFeedRequest 하위 항목들의 타입을 체크하고 각각의 엔티티로 반환
     */
    public Feed toFeed(Member member, UpdateFeedRequest feedRequest) {
        // request에 bookId가 있을 경우만 book을 가져온다.
        Book book = null;
        if (feedRequest.getBookId() != null) {
            book = bookRepository.findById(feedRequest.getBookId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 책이 존재하지 않습니다."));
        }

        // 각 하위 클래스 생성
        if (feedRequest instanceof UpdateFavoriteBookRequest) {
            return new FavoriteBook(member, book,
                    ((UpdateFavoriteBookRequest) feedRequest).getBriefReview(),
                    ((UpdateFavoriteBookRequest) feedRequest).getFullReview());
        }
        if (feedRequest instanceof UpdateMagazineRequest) {
            return new Magazine(member, book, ((UpdateMagazineRequest) feedRequest).getName(),
                    ((UpdateMagazineRequest) feedRequest).getImageUrl(),
                    ((UpdateMagazineRequest) feedRequest).getContent());
        }
        if (feedRequest instanceof UpdateStoryRequest) {
            return new Story(member, book, ((UpdateStoryRequest) feedRequest).getImageUrl(),
                    ((UpdateStoryRequest) feedRequest).getContent());
        }
        throw new IllegalStateException("유효하지 않는 피드 요청입니다.");
    }
}
