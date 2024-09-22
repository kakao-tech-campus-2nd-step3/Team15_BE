package kakao.rebit.wishlist.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kakao.rebit.challenge.entity.Challenge;
import kakao.rebit.member.entity.Member;

@Entity
@DiscriminatorValue("CHALLENGE")
@Table(name = "challenge_wishes")
public class ChallengeWishlist extends Wishlist {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    protected ChallengeWishlist() {
    }

    public ChallengeWishlist(Member member, Challenge challenge) {
        super(member);
        this.challenge = challenge;
    }

    public Challenge getChallenge() {
        return challenge;
    }
}
