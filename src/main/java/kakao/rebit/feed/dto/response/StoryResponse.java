package kakao.rebit.feed.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "스토리 응답")
public class StoryResponse extends FeedResponse {

    private final String imageKey;
    private final String presignedUrl;
    private final String content;

    public StoryResponse(
            @JsonProperty("id") Long id,
            @JsonProperty("author") FeedAuthorResponse author,
            @JsonProperty("book") FeedBookResponse book,
            @JsonProperty("type") String type,
            @JsonProperty("likes") int likes,
            @JsonProperty("isLiked") boolean isLiked,
            @JsonProperty("imageKey") String imageKey,
            @JsonProperty("presignedUrl") String presignedUrl,
            @JsonProperty("content") String content
    ) {
        super(id, author, book, type, likes, isLiked);
        this.imageKey = imageKey;
        this.presignedUrl = presignedUrl;
        this.content = content;
    }

    public String getImageKey() {
        return imageKey;
    }

    public String getPresignedUrl() {
        return presignedUrl;
    }

    public String getContent() {
        return content;
    }
}
