package kakao.rebit.challenge.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyInt;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.doCallRealMethod;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.verify;

import java.util.Optional;
import kakao.rebit.challenge.dto.ChallengeParticipationRequest;
import kakao.rebit.challenge.entity.Challenge;
import kakao.rebit.challenge.entity.ChallengeParticipation;
import kakao.rebit.challenge.exception.challenge.ChallengeErrorCode;
import kakao.rebit.challenge.exception.challenge.EntryFeeNotEnoughException;
import kakao.rebit.challenge.exception.challenge.FullException;
import kakao.rebit.challenge.exception.challenge.NotRecruitingException;
import kakao.rebit.challenge.exception.participation.ParticipationAlreadyExistsException;
import kakao.rebit.challenge.exception.participation.ParticipationErrorCode;
import kakao.rebit.challenge.exception.participation.ParticipationNotParticipantException;
import kakao.rebit.challenge.fixture.ChallengeParticipationFixture;
import kakao.rebit.challenge.repository.ChallengeParticipationRepository;
import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.fixture.MemberFixture;
import kakao.rebit.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("챌린지 참여 서비스 테스트")
class ChallengeParticipationServiceTest {

    @InjectMocks
    private ChallengeParticipationService challengeParticipationService;

    @Mock
    private MemberService memberService;

    @Mock
    private ChallengeService challengeService;

    @Mock
    private ChallengeParticipationRepository challengeParticipationRepository;

    private Member member;
    private Challenge challenge;

    @BeforeEach
    void setUp() {
        member = mock(Member.class);
        given(member.getId()).willReturn(1L);

        challenge = mock(Challenge.class);
    }

    @Test
    void 챌린지_참여() {
        //given
        doNothing().when(member).usePoints(anyInt());

        MemberResponse memberResponse = MemberFixture.toMemberResponse(member);
        ChallengeParticipationRequest participationRequest =
                ChallengeParticipationFixture.createRequest(challenge.getMinimumEntryFee());
        ChallengeParticipation participation =
                ChallengeParticipationFixture.createDefault(challenge, member, participationRequest.entryFee());

        given(memberService.findMemberByIdOrThrow(anyLong())).willReturn(member);
        given(challengeService.findChallengeByIdOrThrow(anyLong())).willReturn(challenge);
        given(challengeParticipationRepository.existsByChallengeAndMember(any(), any())).willReturn(false);
        given(challengeParticipationRepository.save(any())).willReturn(participation);

        //when
        challengeParticipationService.createChallengeParticipation(memberResponse, challenge.getId(), participationRequest);

        //then
        verify(memberService).findMemberByIdOrThrow(anyLong());
        verify(challengeService).findChallengeByIdOrThrow(anyLong());
        verify(member).usePoints(anyInt());
        verify(challengeParticipationRepository).save(any());
    }

    @Test
    void 이미_참여한_챌린지에는_참여할_수_없다() {
        //given
        given(challenge.getId()).willReturn(1L);

        MemberResponse memberResponse = MemberFixture.toMemberResponse(member);
        ChallengeParticipationRequest participationRequest =
                ChallengeParticipationFixture.createRequest(challenge.getMinimumEntryFee());

        given(memberService.findMemberByIdOrThrow(anyLong())).willReturn(member);
        given(challengeService.findChallengeByIdOrThrow(anyLong())).willReturn(challenge);
        given(challengeParticipationRepository.existsByChallengeAndMember(any(), any())).willReturn(true);

        //when, then
        assertThatThrownBy(() -> challengeParticipationService.createChallengeParticipation(
                memberResponse,
                challenge.getId(),
                participationRequest)
        )
                .isInstanceOf(ParticipationAlreadyExistsException.class)
                .hasMessage(ParticipationErrorCode.ALREADY_EXISTS.getMessage());
    }

    @Test
    void 최소_입장료_미만으로_참여할_수_없다() {
        //given
        given(challenge.getMinimumEntryFee()).willReturn(1000);
        given(challenge.isRecruiting(any())).willReturn(true);
        given(challenge.isFull()).willReturn(false);

        // Challenge의 검증 메서드가 실제로 호출되도록 설정
        doCallRealMethod().when(challenge).validateParticipate(anyInt());

        MemberResponse memberResponse = MemberFixture.toMemberResponse(member);
        ChallengeParticipationRequest participationRequest =
                ChallengeParticipationFixture.createRequest(challenge.getMinimumEntryFee() - 1);

        given(memberService.findMemberByIdOrThrow(anyLong())).willReturn(member);
        given(challengeService.findChallengeByIdOrThrow(anyLong())).willReturn(challenge);
        given(challengeParticipationRepository.existsByChallengeAndMember(any(), any())).willReturn(false);

        //when, then
        assertThatThrownBy(() -> challengeParticipationService.createChallengeParticipation(
                memberResponse,
                challenge.getId(),
                participationRequest)
        )
                .isInstanceOf(EntryFeeNotEnoughException.class)
                .hasMessage(ChallengeErrorCode.ENTRY_FEE_NOT_ENOUGH.getMessage());
    }

    @Test
    void 챌린지가_모집중이_아닌_경우_참여할_수_없다() {
        //given
        given(challenge.getId()).willReturn(1L);
        given(challenge.isRecruiting(any())).willReturn(false); // 모집중이 아님

        // Challenge의 검증 메서드가 실제로 호출되도록 설정
        doCallRealMethod().when(challenge).validateParticipate(anyInt());

        MemberResponse memberResponse = MemberFixture.toMemberResponse(member);
        ChallengeParticipationRequest participationRequest =
                ChallengeParticipationFixture.createRequest(challenge.getMinimumEntryFee());

        given(memberService.findMemberByIdOrThrow(anyLong())).willReturn(member);
        given(challengeService.findChallengeByIdOrThrow(anyLong())).willReturn(challenge);
        given(challengeParticipationRepository.existsByChallengeAndMember(any(), any())).willReturn(false);

        //when, then
        assertThatThrownBy(() -> challengeParticipationService.createChallengeParticipation(
                memberResponse,
                challenge.getId(),
                participationRequest)
        )
                .isInstanceOf(NotRecruitingException.class)
                .hasMessage(ChallengeErrorCode.NOT_RECRUITING.getMessage());
    }

    @Test
    void 챌린지_인원이_꽉찬_경우_참여할_수_없다() {
        //given
        given(challenge.getId()).willReturn(1L);
        given(challenge.isRecruiting(any())).willReturn(true);
        given(challenge.isFull()).willReturn(true); // 인원 꽉 참

        // Challenge의 검증 메서드가 실제로 호출되도록 설정
        doCallRealMethod().when(challenge).validateParticipate(anyInt());

        MemberResponse memberResponse = MemberFixture.toMemberResponse(member);
        ChallengeParticipationRequest participationRequest =
                ChallengeParticipationFixture.createRequest(challenge.getMinimumEntryFee());

        given(memberService.findMemberByIdOrThrow(anyLong())).willReturn(member);
        given(challengeService.findChallengeByIdOrThrow(anyLong())).willReturn(challenge);
        given(challengeParticipationRepository.existsByChallengeAndMember(any(), any())).willReturn(false);

        //when, then
        assertThatThrownBy(() -> challengeParticipationService.createChallengeParticipation(
                memberResponse,
                challenge.getId(),
                participationRequest)
        )
                .isInstanceOf(FullException.class)
                .hasMessage(ChallengeErrorCode.FULL.getMessage());
    }

    @Test
    void 참여하지_않은_챌린지는_취소할_수_없다() {
        //given
        Member anotherMember = mock(Member.class);
        given(anotherMember.getId()).willReturn(2L);

        ChallengeParticipation participation = mock(ChallengeParticipation.class);
        given(challengeParticipationRepository.findById(anyLong())).willReturn(Optional.of(participation));
        given(participation.getMember()).willReturn(anotherMember);

        assertThatThrownBy(() -> challengeParticipationService.cancelParticipation(MemberFixture.toMemberResponse(member), 1L))
            .isInstanceOf(ParticipationNotParticipantException.class)
            .hasMessage(ParticipationErrorCode.NOT_PARTICIPANT.getMessage());
    }
}
