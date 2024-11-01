package kakao.rebit.challenge.entity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import kakao.rebit.challenge.exception.challenge.ChallengeErrorCode;
import kakao.rebit.challenge.exception.challenge.EntryFeeNotEnoughException;
import kakao.rebit.challenge.exception.challenge.NotRecruitingException;
import kakao.rebit.challenge.fixture.ChallengeFixture;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.fixture.MemberFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ChallengeParticipationTest {

    @Test
    void 참여자는_챌린지_최소_입장료_이상으로_참여_가능하다() {
        //given
        Member challengeAuthor = MemberFixture.createDefault();
        Challenge challenge = ChallengeFixture.createRecruiting(challengeAuthor);

        Member participant = MemberFixture.createDefault();
        Integer entryFee = challenge.getMinimumEntryFee();

        //when, then
        assertDoesNotThrow(() -> ChallengeParticipation.of(challenge, participant, entryFee));
    }

    @Test
    void 모집기간이_아니라면_참여가_불가능하다() {
        //given
        Member challengeAuthor = MemberFixture.createDefault();
        Challenge challenge = ChallengeFixture.createRecruitmentEnded(challengeAuthor);

        Member participant = MemberFixture.createDefault();
        Integer entryFee = challenge.getMinimumEntryFee();

        //when, then
        Assertions.assertThatThrownBy(() -> ChallengeParticipation.of(challenge, participant, entryFee))
                .isInstanceOf(NotRecruitingException.class)
                .hasMessage(ChallengeErrorCode.NOT_RECRUITING.getMessage());
    }

    @Test
    void 완료된_챌린지는_참여가_불가능하다() {
        //given
        Member challengeAuthor = MemberFixture.createDefault();
        Challenge challenge = ChallengeFixture.createCompleted(challengeAuthor);

        Member participant = MemberFixture.createDefault();
        Integer entryFee = challenge.getMinimumEntryFee();

        //when, then
        Assertions.assertThatThrownBy(() -> ChallengeParticipation.of(challenge, participant, entryFee))
                .isInstanceOf(NotRecruitingException.class)
                .hasMessage(ChallengeErrorCode.NOT_RECRUITING.getMessage());
    }

    @Test
    void 참여자는_챌린지_최소_입장료_미만으로_참여가_불가능하다() {
        //given
        Member challengeAuthor = MemberFixture.createDefault();
        Challenge challenge = ChallengeFixture.createRecruiting(challengeAuthor);

        Member participant = MemberFixture.createDefault();
        Integer entryFee = challenge.getMinimumEntryFee() - 1;

        //when, then
        Assertions.assertThatThrownBy(() -> ChallengeParticipation.of(challenge, participant, entryFee))
                .isInstanceOf(EntryFeeNotEnoughException.class)
                .hasMessage(ChallengeErrorCode.ENTRY_FEE_NOT_ENOUGH.getMessage());
    }
}
