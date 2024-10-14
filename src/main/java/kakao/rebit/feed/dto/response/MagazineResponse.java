package kakao.rebit.feed.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "메거진 응답")
public class MagazineResponse extends FeedResponse {

    private final String name;
    private final String imagekey;
    private final String presignedUrl;
    private final String content;

    public MagazineResponse(Long id, FeedAuthorResponse author, FeedBookResponse book, String type,
            int likes, String name, String imagekey, String presignedUrl, String content) {
        super(id, author, book, type, likes);
        this.name = name;
        this.imagekey = imagekey;
        this.presignedUrl = presignedUrl;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getImagekey() {
        return imagekey;
    }

    public String getPresignedUrl() {
        return presignedUrl;
    }

    public String getContent() {
        return content;
    }
}
