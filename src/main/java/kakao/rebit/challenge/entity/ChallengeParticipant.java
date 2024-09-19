package kakao.rebit.challenge.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kakao.rebit.common.persistence.BaseEntity;
import kakao.rebit.member.entity.Member;

@Entity
public class ChallengeParticipant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private Integer entryFee;

    protected ChallengeParticipant() {
    }

    public ChallengeParticipant(Challenge challenge, Member member, Integer entryFee) {
        this.challenge = challenge;
        this.member = member;
        this.entryFee = entryFee;
    }

    public Long getId() {
        return id;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public Member getMember() {
        return member;
    }

    public Integer getEntryFee() {
        return entryFee;
    }
}
