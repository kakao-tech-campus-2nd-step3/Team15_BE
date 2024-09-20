package kakao.rebit.wishlist.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import kakao.rebit.common.persistence.BaseEntity;

@Entity
@Table(name = "book_wishes")
public class BookWishlist extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wishlist_id")
    private Wishlist wishlist;

    private String isbn;

    protected BookWishlist() {
    }

    public BookWishlist(Wishlist wishlist, String isbn) {
        this.wishlist = wishlist;
        this.isbn = isbn;
    }

    public Long getId() {
        return id;
    }

    public Wishlist getWishlist() {
        return wishlist;
    }

    public String getIsbn() {
        return isbn;
    }
}

