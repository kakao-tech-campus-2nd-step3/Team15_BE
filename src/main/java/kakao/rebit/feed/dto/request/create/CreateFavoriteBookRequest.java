package kakao.rebit.feed.dto.request.create;

import jakarta.validation.constraints.NotBlank;

public class CreateFavoriteBookRequest extends CreateFeedRequest {

    @NotBlank(message = "한줄평은 필수 입력 값입니다.")
    private String briefReview;
    @NotBlank(message = "서평은 필수 입력 값입니다.")
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
