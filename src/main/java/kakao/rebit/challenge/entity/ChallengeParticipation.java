package kakao.rebit.challenge.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import kakao.rebit.common.persistence.BaseEntity;
import kakao.rebit.member.entity.Member;

@Entity
@Table(name = "challenge_participation")
public class ChallengeParticipation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Integer entryFee;

    @OneToMany(mappedBy = "challengeParticipation", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private final List<ChallengeVerification> challengeVerifications = new ArrayList<>();

    protected ChallengeParticipation() {
    }

    private ChallengeParticipation(Challenge challenge, Member member, Integer entryFee) {
        challenge.validateParticipate(entryFee);
        this.challenge = challenge;
        this.member = member;
        this.entryFee = entryFee;
    }

    public static ChallengeParticipation of(Challenge challenge, Member member, Integer entryFee) {
        return new ChallengeParticipation(challenge, member, entryFee);
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
