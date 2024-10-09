package kakao.rebit.feed.dto.response;

public class StoryResponse extends FeedResponse {

    private String presignedUrl;
    private String content;

    public StoryResponse(Long id, FeedAuthorResponse author, FeedBookResponse book,
            String type, int likes, String presignedUrl, String content) {
        super(id, author, book, type, likes);
        this.presignedUrl = presignedUrl;
        this.content = content;
    }

    public String getImagePresignedUrl() {
        return presignedUrl;
    }

    public String getContent() {
        return content;
    }
}
