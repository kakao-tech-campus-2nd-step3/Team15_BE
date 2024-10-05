package kakao.rebit.feed.dto.response;

public class StoryResponse extends FeedResponse {

    private String imageUrl;
    private String content;

    public StoryResponse(Long id, AuthorResponse author, FeedBookResponse book,
            String type, int likes, String imageUrl, String content) {
        super(id, author, book, type, likes);
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
