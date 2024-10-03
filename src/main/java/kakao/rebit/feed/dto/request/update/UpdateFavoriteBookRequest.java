package kakao.rebit.feed.dto.request.update;

public class UpdateFavoriteBookRequest extends UpdateFeedRequest {

    private String briefReview;

    private String fullReview;

    private UpdateFavoriteBookRequest() {
    }

    public UpdateFavoriteBookRequest(Long bookId, String briefReview, String fullReview) {
        super(bookId);
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
