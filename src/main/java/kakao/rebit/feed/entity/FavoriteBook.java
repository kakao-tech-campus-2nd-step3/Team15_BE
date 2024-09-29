package kakao.rebit.feed.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import kakao.rebit.book.entity.Book;
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
    public void updateNonNullFields(Feed feed) {
        if (feed.getBook() != null) {
            super.updateNonNullFields(feed);
        }
        if (((FavoriteBook) feed).getBriefReview() != null) {
            this.briefReview = ((FavoriteBook) feed).getBriefReview();
        }
        if (((FavoriteBook) feed).getFullReview() != null) {
            this.fullReview = ((FavoriteBook) feed).getFullReview();
        }
    }

    public String getBriefReview() {
        return briefReview;
    }

    public String getFullReview() {
        return fullReview;
    }
}
