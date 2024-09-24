package kakao.rebit.wishlist.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kakao.rebit.book.entity.Book;
import kakao.rebit.member.entity.Member;

@Entity
@DiscriminatorValue("BOOK")
@Table(name = "book_wishlist")
public class BookWishlist extends Wishlist {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "isbn")
    private Book book;

    protected BookWishlist() {
    }

    public BookWishlist(Member member, Book book) {
        super(member);
        this.book = book;
    }

    public Book getBook() {
        return book;
    }
}
