package kakao.rebit.feed.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import kakao.rebit.book.entity.Book;
import kakao.rebit.common.persistence.BaseEntity;
import kakao.rebit.feed.dto.request.update.UpdateFeedRequest;
import kakao.rebit.member.entity.Member;
import org.hibernate.annotations.Formula;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
@Table(name = "feed")
public abstract class Feed extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(SELECT COUNT(1) FROM likes l WHERE l.feed_id = id)")
    private int likes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "type", insertable = false, updatable = false)
    private String type; // 상속 관계로 인해 생성된 type을 조회하기 위한 필드

    @OneToMany(mappedBy = "feed", cascade = CascadeType.REMOVE)
    private List<Likes> likesList = new ArrayList<>();

    protected Feed() {
    }

    protected Feed(Member member, Book book) {
        this.member = member;
        this.book = book;
    }

    public boolean isWrittenBy(Member member) {
        return this.member.equals(member);
    }

    public void changeBook(Book book) {
        this.book = book;
    }

    public abstract void updateAllExceptBook(UpdateFeedRequest feedRequest);

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public int getLikes() {
        return likes;
    }

    public Book getBook() {
        return book;
    }

    public String getType() {
        return type;
    }

}
