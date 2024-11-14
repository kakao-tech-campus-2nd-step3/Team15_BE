package kakao.rebit.feed.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import java.beans.ConstructorProperties;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = FavoriteBookResponse.class, name = "FB"),
        @JsonSubTypes.Type(value = MagazineResponse.class, name = "M"),
        @JsonSubTypes.Type(value = StoryResponse.class, name = "S")
})
@Schema(oneOf = {FavoriteBookResponse.class, MagazineResponse.class, StoryResponse.class})
public abstract class FeedResponse {

    private final Long id;
    private final FeedAuthorResponse author;
    private final FeedBookResponse book;
    private final String type;
    private final int likes;
    @JsonProperty("isLiked")
    private final boolean isLiked;

    public FeedResponse(
            @JsonProperty("id") Long id,
            @JsonProperty("author") FeedAuthorResponse author,
            @JsonProperty("book") FeedBookResponse book,
            @JsonProperty("type") String type,
            @JsonProperty("likes") int likes,
            @JsonProperty("isLiked") boolean isLiked
    ) {
        this.id = id;
        this.author = author;
        this.book = book;
        this.type = type;
        this.likes = likes;
        this.isLiked = isLiked;
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

    public boolean getIsLiked() {
        return isLiked;
    }
}
