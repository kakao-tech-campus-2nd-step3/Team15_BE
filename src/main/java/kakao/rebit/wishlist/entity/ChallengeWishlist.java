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
import kakao.rebit.challenge.entity.Challenge;

@Entity
@Table(name = "challenge_wishes")
public class ChallengeWishlist extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wishlist_id")
    private Wishlist wishlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    protected ChallengeWishlist() {
    }

    public ChallengeWishlist(Wishlist wishlist, Challenge challenge) {
        this.wishlist = wishlist;
        this.challenge = challenge;
    }

    public Long getId() {
        return id;
    }

    public Wishlist getWishlist() {
        return wishlist;
    }

    public Challenge getChallenge() {
        return challenge;
    }
}
