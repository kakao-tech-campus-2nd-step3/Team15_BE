package kakao.rebit.feed.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import kakao.rebit.book.entity.Book;
import kakao.rebit.member.entity.Member;

@Entity
@DiscriminatorValue("FB")
public class FavoriteBook extends Feed {

    private String brief_review;

    private String full_review;

    protected FavoriteBook() {
    }

    public FavoriteBook(Member member, Book book, String brief_review,
            String full_review) {
        super(member, book);
        this.brief_review = brief_review;
        this.full_review = full_review;
    }

    public String getBrief_review() {
        return brief_review;
    }

    public String getFull_review() {
        return full_review;
    }
}
