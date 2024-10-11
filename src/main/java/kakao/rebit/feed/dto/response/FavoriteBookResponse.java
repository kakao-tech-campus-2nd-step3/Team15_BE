package kakao.rebit.feed.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "인생책 응답")
public class FavoriteBookResponse extends FeedResponse {

    private String briefReview;
    private String fullReview;

    public FavoriteBookResponse(Long id, FeedAuthorResponse author, FeedBookResponse book,
            String type,
            int likes,
            String briefReview,
            String fullReview) {
        super(id, author, book, type, likes);
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
