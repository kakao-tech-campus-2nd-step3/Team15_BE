package kakao.rebit.feed.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "인생책 응답")
public class FavoriteBookResponse extends FeedResponse {

    private final String briefReview;
    private final String fullReview;

    public FavoriteBookResponse(
            @JsonProperty("id") Long id,
            @JsonProperty("author") FeedAuthorResponse author,
            @JsonProperty("book") FeedBookResponse book,
            @JsonProperty("type") String type,
            @JsonProperty("likes") int likes,
            @JsonProperty("isLiked") boolean isLiked,
            @JsonProperty("briefReview") String briefReview,
            @JsonProperty("fullReview") String fullReview) {
        super(id, author, book, type, likes, isLiked);
        this.briefReview = briefReview;
        this.fullReview = fullReview;
    }

    public String getBriefReview() {
        return briefReview;
    }

    public String getFullReview() {
        return fullReview;
    }
}
