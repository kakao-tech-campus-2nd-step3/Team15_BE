package kakao.rebit.feed.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "메거진 응답")
public class MagazineResponse extends FeedResponse {

    private final String name;
    private final String imageKey;
    private final String presignedUrl;
    private final String content;

    public MagazineResponse(
            @JsonProperty("id") Long id,
            @JsonProperty("author") FeedAuthorResponse author,
            @JsonProperty("book") FeedBookResponse book,
            @JsonProperty("type") String type,
            @JsonProperty("likes") int likes,
            @JsonProperty("isLiked") boolean isLiked,
            @JsonProperty("name") String name,
            @JsonProperty("imageKey") String imageKey,
            @JsonProperty("presignedUrl") String presignedUrl,
            @JsonProperty("content") String content
    ) {
        super(id, author, book, type, likes, isLiked);
        this.name = name;
        this.imageKey = imageKey;
        this.presignedUrl = presignedUrl;
        this.content = content;
    }

    public String getName() {
        return name;
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
