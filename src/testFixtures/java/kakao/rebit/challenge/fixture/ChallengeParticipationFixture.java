package kakao.rebit.challenge.fixture;

import kakao.rebit.challenge.dto.ChallengeParticipationRequest;
import kakao.rebit.challenge.entity.Challenge;
import kakao.rebit.challenge.entity.ChallengeParticipation;
import kakao.rebit.member.entity.Member;

public class ChallengeParticipationFixture {

    public static ChallengeParticipation createDefault(Challenge challenge, Member member, Integer entryFee) {
        return ChallengeParticipation.of(challenge, member, entryFee);
    }

    public static ChallengeParticipationRequest createRequest(Integer entryFee) {
        return new ChallengeParticipationRequest(entryFee);
    }

}
