package kakao.rebit.feed.dto.request.update;

public class UpdateMagazineRequest extends UpdateFeedRequest {

    private String name;
    private String imageUrl;
    private String content;

    private UpdateMagazineRequest() {
    }

    public UpdateMagazineRequest(Long bookId, String name, String imageUrl, String content) {
        super(bookId);
        this.name = name;
        this.imageUrl = imageUrl;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getContent() {
        return content;
    }
}
