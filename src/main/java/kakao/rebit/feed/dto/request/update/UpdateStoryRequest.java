package kakao.rebit.feed.dto.request.update;

public class UpdateStoryRequest extends UpdateFeedRequest {

    private String imageUrl;
    private String content;

    private UpdateStoryRequest() {
    }

    public UpdateStoryRequest(Long bookId, String imageUrl, String content) {
        super(bookId);
        this.imageUrl = imageUrl;
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getContent() {
        return content;
    }
}
