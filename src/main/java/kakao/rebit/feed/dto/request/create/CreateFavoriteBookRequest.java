package kakao.rebit.feed.dto.request.create;

public class CreateFavoriteBookRequest extends CreateFeedRequest {

    private String briefReview;
    private String fullReview;

    private CreateFavoriteBookRequest() {
    }

    public CreateFavoriteBookRequest(Long bookId, String briefReview, String fullReview) {
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
