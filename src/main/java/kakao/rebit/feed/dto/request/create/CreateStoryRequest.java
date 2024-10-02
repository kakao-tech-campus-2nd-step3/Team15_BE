package kakao.rebit.feed.dto.request.create;

public class CreateStoryRequest extends CreateFeedRequest {

    private String imageUrl;
    private String content;

    private CreateStoryRequest() {
    }

    public CreateStoryRequest(Long bookId, String imageUrl, String content) {
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
