package kakao.rebit.book.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String isbn;
    private String userId;
    private String username;
    private String briefReview;

    protected Review() {}

    public Review(String isbn, String userId, String username, String briefReview) {
        this.isbn = isbn;
        this.userId = userId;
        this.username = username;
        this.briefReview = briefReview;
    }

    public Long getId() {
        return id;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getBriefReview() {
        return briefReview;
    }
}
