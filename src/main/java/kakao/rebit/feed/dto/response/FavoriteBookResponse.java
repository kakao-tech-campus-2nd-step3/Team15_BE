package kakao.rebit.feed.dto.response;

public class FavoriteBookResponse extends FeedResponse {

    private String briefReview;
    private String fullReview;

    public FavoriteBookResponse(Long id, AuthorResponse author, FeedBookResponse book,
            String type,
            String briefReview,
            String fullReview) {
        super(id, author, book, type);
        this.briefReview = briefReview;
        this.fullReview = fullReview;
    }

    public String getBriefReview() {
        return briefReview;
    }

    public String getFullReview() {
        return fullReview;
    }
}
