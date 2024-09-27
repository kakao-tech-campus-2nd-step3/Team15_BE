package kakao.rebit.feed.dto.response;

public abstract class FeedResponse {

    private Long id;
    private AuthorResponse author;
    private BookResponse book;
    private String type;

    public FeedResponse(Long id, AuthorResponse author, BookResponse book,
            String type) {
        this.id = id;
        this.author = author;
        this.book = book;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public AuthorResponse getAuthor() {
        return author;
    }

    public BookResponse getBook() {
        return book;
    }

    public String getType() {
        return type;
    }
}
