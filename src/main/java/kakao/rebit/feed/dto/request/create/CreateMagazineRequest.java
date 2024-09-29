package kakao.rebit.feed.dto.request.create;

public class CreateMagazineRequest extends CreateFeedRequest {

    private String name;
    private String imageUrl;
    private String content;

    private CreateMagazineRequest() {
    }

    public CreateMagazineRequest(Long bookId, String name, String imageUrl, String content) {
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
