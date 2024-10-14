package kakao.rebit.feed.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(oneOf = {FavoriteBookResponse.class, MagazineResponse.class, StoryResponse.class})
public abstract class FeedResponse {

    private final Long id;
    private final FeedAuthorResponse author;
    private final FeedBookResponse book;
    private final String type;
    private final int likes;

    public FeedResponse(Long id, FeedAuthorResponse author, FeedBookResponse book,
            String type, int likes) {
        this.id = id;
        this.author = author;
        this.book = book;
        this.type = type;
        this.likes = likes;
    }

    public Long getId() {
        return id;
    }

    public FeedAuthorResponse getAuthor() {
        return author;
    }

    public FeedBookResponse getBook() {
        return book;
    }

    public String getType() {
        return type;
    }

    public int getLikes() {
        return likes;
    }
}
