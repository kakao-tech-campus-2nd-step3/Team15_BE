package kakao.rebit.feed.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import kakao.rebit.book.entity.Book;
import kakao.rebit.member.entity.Member;

@Entity
@DiscriminatorValue("FB")
public class FavoriteBook extends Feed {

    @Column(length = 1000)
    private String briefReview;

    @Column(length = 3000)
    private String fullReview;

    protected FavoriteBook() {
    }

    public FavoriteBook(Member member, Book book, String briefReview,
            String fullReview) {
        super(member, book);
        this.briefReview = briefReview;
        this.fullReview = fullReview;
    }

    public void updateTextFields(String briefReview, String fullReview) {
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
