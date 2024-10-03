package kakao.rebit.feed.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kakao.rebit.common.persistence.BaseEntity;
import kakao.rebit.member.entity.Member;

@Entity
@Table(name = "likes")
public class Likes extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    protected Likes() {
    }

    public Likes(Member member, Feed feed) {
        this.member = member;
        this.feed = feed;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Feed getFeed() {
        return feed;
    }
}
