package kakao.rebit.book.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kakao.rebit.common.persistence.BaseEntity;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "book")
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String isbn;

    private String title;

    @Column(length = 500)
    private String description;

    private String author;

    private String publisher;

    private String cover;

    @Column(name = "pub_date")
    private String pubDate;

    protected Book() {
    }

    public Book(String isbn, String title, String description, String author, String publisher,
        String cover, String pubDate) {
        this.isbn = isbn;
        this.title = title;
        this.description = description;
        this.author = author;
        this.publisher = publisher;
        this.cover = cover;
        this.pubDate = pubDate;
    }

    public Long getId() {
        return id;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getCover() {
        return cover;
    }

    public String getPubDate() {
        return pubDate;
    }
}
