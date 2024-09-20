package kakao.rebit.wishlist.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kakao.rebit.member.entity.Member;

@Entity
@DiscriminatorValue("BOOK")
@Table(name = "book_wishes")
public class BookWishlist extends Wishlist {

    private String isbn;

    protected BookWishlist() {
    }

    public BookWishlist(Member member, String isbn) {
        super(member);
        this.isbn = isbn;
    }

    public String getIsbn() {
        return isbn;
    }
}

