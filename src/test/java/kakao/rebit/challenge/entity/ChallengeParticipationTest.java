package kakao.rebit.challenge.entity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import kakao.rebit.challenge.exception.challenge.ChallengeErrorCode;
import kakao.rebit.challenge.exception.challenge.EntryFeeNotEnoughException;
import kakao.rebit.challenge.exception.challenge.NotRecruitingException;
import kakao.rebit.challenge.fixture.ChallengeFixture;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.fixture.MemberFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("챌린지 참여 테스트")
class ChallengeParticipationTest {

    private Member challengeAuthor;
    private Member participant;

    @BeforeEach
    void setUp() {
        challengeAuthor = MemberFixture.createDefault();
        participant = MemberFixture.createDefault();
    }

    @Test
    void 참여자는_챌린지_최소_입장료_이상으로_참여_가능하다() {
        //given
        Challenge challenge = ChallengeFixture.createRecruiting(challengeAuthor);

        Integer entryFee = challenge.getMinimumEntryFee();

        //when, then
        assertDoesNotThrow(() -> ChallengeParticipation.of(challenge, participant, entryFee));
    }

    @Test
    void 모집기간이_아니라면_참여가_불가능하다() {
        //given
        Challenge challenge = ChallengeFixture.createRecruitmentEnded(challengeAuthor);

        Integer entryFee = challenge.getMinimumEntryFee();

        //when, then
        assertThatThrownBy(() -> ChallengeParticipation.of(challenge, participant, entryFee))
                .isInstanceOf(NotRecruitingException.class)
                .hasMessage(ChallengeErrorCode.NOT_RECRUITING.getMessage());
    }

    @Test
    void 완료된_챌린지는_참여가_불가능하다() {
        //given
        Challenge challenge = ChallengeFixture.createCompleted(challengeAuthor);

        Integer entryFee = challenge.getMinimumEntryFee();

        //when, then
        assertThatThrownBy(() -> ChallengeParticipation.of(challenge, participant, entryFee))
                .isInstanceOf(NotRecruitingException.class)
                .hasMessage(ChallengeErrorCode.NOT_RECRUITING.getMessage());
    }

    @Test
    void 참여자는_챌린지_최소_입장료_미만으로_참여가_불가능하다() {
        //given
        Challenge challenge = ChallengeFixture.createRecruiting(challengeAuthor);

        Integer entryFee = challenge.getMinimumEntryFee() - 1;

        //when, then
        assertThatThrownBy(() -> ChallengeParticipation.of(challenge, participant, entryFee))
                .isInstanceOf(EntryFeeNotEnoughException.class)
                .hasMessage(ChallengeErrorCode.ENTRY_FEE_NOT_ENOUGH.getMessage());
    }
}
