package kakao.rebit.feed.dto.response;

public abstract class FeedResponse {

    private Long id;
    private FeedAuthorResponse author;
    private FeedBookResponse book;
    private String type;
    private int likes;

    public FeedResponse(Long id, FeedAuthorResponse author, FeedBookResponse book,
            String type, int likes) {
        this.id = id;
        this.author = author;
        this.book = book;
        this.type = type;
        this.likes = likes;
    }

    public Long getId() {
        return id;
    }

    public FeedAuthorResponse getAuthor() {
        return author;
    }

    public FeedBookResponse getBook() {
        return book;
    }

    public String getType() {
        return type;
    }

    public int getLikes() {
        return likes;
    }
}
