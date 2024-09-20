package kakao.rebit.book.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kakao.rebit.common.persistence.BaseEntity;

@Entity
@Table(name = "book")
public class Book extends BaseEntity {

    @Id
    private String isbn;

    private String title;

    @Column(length = 500)
    private String description;

    private String author;

    private String publisher;

    @Column(name = "image_url")
    private String imageUrl;

    protected Book() {
    }

    public Book(String isbn, String title, String description, String author, String publisher, String imageUrl) {
        this.isbn = isbn;
        this.title = title;
        this.description = description;
        this.author = author;
        this.publisher = publisher;
        this.imageUrl = imageUrl;
    }

    public String getIsbn() { return isbn; }

    public String getTitle() { return title; }

    public String getDescription() { return description; }

    public String getAuthor() { return author; }

    public String getPublisher() { return publisher; }

    public String getImageUrl() { return imageUrl; }
}