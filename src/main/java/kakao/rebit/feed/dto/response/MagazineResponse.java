package kakao.rebit.feed.dto.response;

public class MagazineResponse extends FeedResponse {

    private String name;
    private String presignedUrl;
    private String content;

    public MagazineResponse(Long id, FeedAuthorResponse author, FeedBookResponse book,
            String type, int likes, String name, String presignedUrl, String content) {
        super(id, author, book, type, likes);
        this.name = name;
        this.presignedUrl = presignedUrl;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getPresignedUrl() {
        return presignedUrl;
    }

    public String getContent() {
        return content;
    }
}
