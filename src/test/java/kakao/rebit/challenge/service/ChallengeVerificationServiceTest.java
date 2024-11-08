package kakao.rebit.challenge.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import kakao.rebit.challenge.dto.ChallengeVerificationRequest;
import kakao.rebit.challenge.entity.Challenge;
import kakao.rebit.challenge.entity.ChallengeParticipation;
import kakao.rebit.challenge.entity.ChallengeVerification;
import kakao.rebit.challenge.exception.verification.DeleteNotAuthorizedException;
import kakao.rebit.challenge.exception.verification.VerificationErrorCode;
import kakao.rebit.challenge.exception.verification.VerifyChallengeNotOngoingException;
import kakao.rebit.challenge.fixture.ChallengeVerificationFixture;
import kakao.rebit.challenge.repository.ChallengeVerificationRepository;
import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.service.MemberService;
import kakao.rebit.s3.service.S3Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("챌린지 인증글 서비스 테스트")
class ChallengeVerificationServiceTest {

    @InjectMocks
    private ChallengeVerificationService challengeVerificationService;

    @Mock
    private ChallengeService challengeService;

    @Mock
    private MemberService memberService;

    @Mock
    private ChallengeVerificationRepository challengeVerificationRepository;

    @Mock
    private ChallengeParticipationService challengeParticipationService;

    @Mock
    private S3Service s3Service;
    
    @Test
    void 챌린지_인증글_작성() {
        // given
        MemberResponse memberResponse = mock(MemberResponse.class);
        given(memberResponse.id()).willReturn(1L);

        Challenge challenge = mock(Challenge.class);
        given(challenge.isOngoing(any())).willReturn(true);

        ChallengeParticipation challengeParticipation = mock(ChallengeParticipation.class);
        given(challengeParticipation.getId()).willReturn(1L);

        ChallengeVerificationRequest verificationRequest = ChallengeVerificationFixture.createRequest();

        given(memberService.findMemberByIdOrThrow(anyLong())).willReturn(mock(Member.class));
        given(challengeService.findChallengeByIdOrThrow(anyLong())).willReturn(challenge);
        given(challengeParticipationService.findChallengeParticipationByMemberAndChallengeOrThrow(any(), any()))
                .willReturn(challengeParticipation);
        given(challengeVerificationRepository.existsDailyVerification(any(), any())).willReturn(false);
        given(challengeVerificationRepository.save(any())).willReturn(mock(ChallengeVerification.class));

        // when
        challengeVerificationService.createChallengeVerification(memberResponse, challenge.getId(), verificationRequest);

        // then
        verify(memberService).findMemberByIdOrThrow(anyLong());
        verify(challengeService).findChallengeByIdOrThrow(anyLong());
        verify(challengeParticipationService).findChallengeParticipationByMemberAndChallengeOrThrow(any(), any());
        verify(challengeVerificationRepository).save(any());
    }

    @Test
    void 진행중이지_않은_챌린지에_인증글을_작성할_수_없다() {
        // given
        MemberResponse memberResponse = mock(MemberResponse.class);
        given(memberResponse.id()).willReturn(1L);

        Challenge challenge = mock(Challenge.class);
        given(challenge.isOngoing(any())).willReturn(false);

        ChallengeVerificationRequest verificationRequest = ChallengeVerificationFixture.createRequest();

        given(memberService.findMemberByIdOrThrow(anyLong())).willReturn(mock(Member.class));
        given(challengeService.findChallengeByIdOrThrow(anyLong())).willReturn(challenge);

        // when, then
        assertThatThrownBy(() -> challengeVerificationService.createChallengeVerification(
                memberResponse,
                challenge.getId(),
                verificationRequest)
        )
                .isInstanceOf(VerifyChallengeNotOngoingException.class)
                .hasMessageContaining(VerificationErrorCode.VERIFY_CHALLENGE_NOT_ON_GOING.getMessage());
    }

    @Test
    void 챌린지_인증글_삭제() {
        // given
        Member member = mock(Member.class);
        given(member.getId()).willReturn(1L);

        MemberResponse memberResponse = mock(MemberResponse.class);
        given(memberResponse.id()).willReturn(1L);

        Challenge challenge = mock(Challenge.class);

        ChallengeParticipation challengeParticipation = mock(ChallengeParticipation.class);
        given(challengeParticipation.getMember()).willReturn(member);

        ChallengeVerification verification = mock(ChallengeVerification.class);
        given(verification.getChallengeParticipation()).willReturn(challengeParticipation);

        given(memberService.findMemberByIdOrThrow(anyLong())).willReturn(member);
        given(challengeService.findChallengeByIdOrThrow(anyLong())).willReturn(challenge);
        given(challengeVerificationRepository.findByIdAndChallengeParticipation_Challenge(anyLong(), any()))
                .willReturn(java.util.Optional.of(verification));
        doNothing().when(challengeVerificationRepository).delete(verification);
        doNothing().when(s3Service).deleteObject(any());

        // when
        challengeVerificationService.deleteChallengeVerification(memberResponse, challenge.getId(), verification.getId());

        // then
        verify(memberService).findMemberByIdOrThrow(anyLong());
        verify(challengeService).findChallengeByIdOrThrow(anyLong());
        verify(challengeVerificationRepository).findByIdAndChallengeParticipation_Challenge(anyLong(), any());
        verify(challengeVerificationRepository).delete(verification);
        verify(s3Service).deleteObject(verification.getImageKey());
    }

    @Test
    void 다른_사람이_작성한_인증글을_삭제할_수_없다() {
        // given
        Member member = mock(Member.class);         // 삭제 요청자
        given(member.getId()).willReturn(1L);

        Member anotherMember = mock(Member.class);  // 작성자
        given(anotherMember.getId()).willReturn(2L);

        MemberResponse memberResponse = mock(MemberResponse.class);
        given(memberResponse.id()).willReturn(1L);

        Challenge challenge = mock(Challenge.class);

        ChallengeParticipation challengeParticipation = mock(ChallengeParticipation.class);
        given(challengeParticipation.getMember()).willReturn(anotherMember);

        ChallengeVerification verification = mock(ChallengeVerification.class);
        given(verification.getChallengeParticipation()).willReturn(challengeParticipation);

        given(memberService.findMemberByIdOrThrow(anyLong())).willReturn(member);
        given(challengeService.findChallengeByIdOrThrow(anyLong())).willReturn(challenge);
        given(challengeVerificationRepository.findByIdAndChallengeParticipation_Challenge(anyLong(), any()))
                .willReturn(java.util.Optional.of(verification));

        // when, then
        assertThatThrownBy(() -> challengeVerificationService.deleteChallengeVerification(
                memberResponse,
                challenge.getId(),
                verification.getId())
        )
                .isInstanceOf(DeleteNotAuthorizedException.class)
                .hasMessageContaining(VerificationErrorCode.DELETE_NOT_AUTHORIZED.getMessage());
    }
}
