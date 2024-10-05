package kakao.rebit.feed.dto.response;

public class MagazineResponse extends FeedResponse {

    private String name;
    private String imageUrl;
    private String content;

    public MagazineResponse(Long id, AuthorResponse author, FeedBookResponse book,
            String type, int likes, String name, String imageUrl, String content) {
        super(id, author, book, type, likes);
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
