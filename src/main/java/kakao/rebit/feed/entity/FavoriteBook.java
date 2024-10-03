package kakao.rebit.feed.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import kakao.rebit.book.entity.Book;
import kakao.rebit.feed.dto.request.update.UpdateFavoriteBookRequest;
import kakao.rebit.feed.dto.request.update.UpdateFeedRequest;
import kakao.rebit.member.entity.Member;

@Entity
@DiscriminatorValue("FB")
public class FavoriteBook extends Feed {

    private String briefReview;
    private String fullReview;

    protected FavoriteBook() {
    }

    public FavoriteBook(Member member, Book book, String briefReview,
            String fullReview) {
        super(member, book);
        this.briefReview = briefReview;
        this.fullReview = fullReview;
    }

    @Override
    public void updateAllExceptBook(UpdateFeedRequest feedRequest) {
        this.briefReview = ((UpdateFavoriteBookRequest) feedRequest).getBriefReview();
        this.fullReview = ((UpdateFavoriteBookRequest) feedRequest).getFullReview();
    }

    public String getBriefReview() {
        return briefReview;
    }

    public String getFullReview() {
        return fullReview;
    }
}
