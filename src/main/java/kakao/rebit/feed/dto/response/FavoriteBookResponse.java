package kakao.rebit.feed.dto.response;

import kakao.rebit.member.dto.MemberResponse;

public class FavoriteBookResponse extends FeedResponse {

    private String briefReview;
    private String fullReview;

    public FavoriteBookResponse(Long id, MemberResponse memberResponse, BookResponse bookResponse,
            String briefReview,
            String fullReview) {
        super(id, memberResponse, bookResponse, Type.FB);
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
