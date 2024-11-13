package kakao.rebit.diary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kakao.rebit.book.entity.Book;
import kakao.rebit.common.persistence.BaseEntity;
import kakao.rebit.member.entity.Member;

@Entity
@Table(name = "diary")
public class Diary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "isbn")
    private Book book;

    protected Diary() {
    }

    public Diary(String content, Member member, Book book) {
        this.content = content;
        this.member = member;
        this.book = book;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Member getMember() {
        return member;
    }

    public Book getBook() {
        return book;
    }

    public void updateDiary(String content, Book book) {
        this.content = content;
        this.book = book;
    }
}
