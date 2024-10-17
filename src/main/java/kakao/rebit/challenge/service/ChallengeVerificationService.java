package kakao.rebit.challenge.service;

import java.time.LocalDateTime;
import kakao.rebit.challenge.dto.AuthorResponse;
import kakao.rebit.challenge.dto.ChallengeVerificationRequest;
import kakao.rebit.challenge.dto.ChallengeVerificationResponse;
import kakao.rebit.challenge.entity.Challenge;
import kakao.rebit.challenge.entity.ChallengeParticipation;
import kakao.rebit.challenge.entity.ChallengeVerification;
import kakao.rebit.challenge.exception.verification.VerifyChallengeAlreadyDoneTodayException;
import kakao.rebit.challenge.exception.verification.VerifyChallengeNotOngoingException;
import kakao.rebit.challenge.exception.verification.DeleteNotAuthorizedException;
import kakao.rebit.challenge.exception.verification.VerificationNotFoundException;
import kakao.rebit.challenge.repository.ChallengeVerificationRepository;
import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.service.MemberService;
import kakao.rebit.s3.service.S3Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChallengeVerificationService {

    private final ChallengeService challengeService;
    private final MemberService memberService;
    private final ChallengeVerificationRepository challengeVerificationRepository;
    private final ChallengeParticipationService challengeParticipationService;
    private final S3Service s3Service;

    public ChallengeVerificationService(ChallengeService challengeService,
            MemberService memberService,
            ChallengeVerificationRepository challengeVerificationRepository,
            ChallengeParticipationService challengeParticipationService, S3Service s3Service) {
        this.challengeService = challengeService;
        this.memberService = memberService;
        this.challengeVerificationRepository = challengeVerificationRepository;
        this.challengeParticipationService = challengeParticipationService;
        this.s3Service = s3Service;
    }

    @Transactional(readOnly = true)
    public Page<ChallengeVerificationResponse> getChallengeVerificationsById(Long challengeId, Pageable pageable) {
        Challenge challenge = challengeService.findChallengeByIdOrThrow(challengeId);
        Page<ChallengeVerification> challengeVerifications = challengeVerificationRepository.findAllByChallengeWithParticipants(challenge, pageable);
        return challengeVerifications.map(this::toChallengeVerificationResponse);
    }

    @Transactional(readOnly = true)
    public ChallengeVerificationResponse getChallengeVerificationById(Long challengeId, Long verificationId) {
        Challenge challenge = challengeService.findChallengeByIdOrThrow(challengeId);
        ChallengeVerification challengeVerification = findByIdAndChallengeOrThrow(verificationId, challenge);
        return toChallengeVerificationResponse(challengeVerification);

    }

    private ChallengeVerification findByIdAndChallengeOrThrow(Long verificationId, Challenge challenge) {
        return challengeVerificationRepository.findByIdAndChallengeParticipation_Challenge(verificationId,
                        challenge)
                .orElseThrow(() -> VerificationNotFoundException.EXCEPTION);
    }

    @Transactional
    public Long createChallengeVerification(MemberResponse memberResponse, Long challengeId,
            ChallengeVerificationRequest challengeVerificationRequest) {
        Member member = memberService.findMemberByIdOrThrow(memberResponse.id());
        Challenge challenge = challengeService.findChallengeByIdOrThrow(challengeId);

        if (!challenge.isOngoing(LocalDateTime.now())) {
            throw VerifyChallengeNotOngoingException.EXCEPTION;
        }

        ChallengeParticipation challengeParticipation = challengeParticipationService.findChallengeParticipationByMemberAndChallengeOrThrow(
                member, challenge);

        if (challengeVerificationRepository.existsDailyVerification(challengeParticipation.getId(), LocalDateTime.now())) {
            throw VerifyChallengeAlreadyDoneTodayException.EXCEPTION;
        }

        return challengeVerificationRepository.save(toChallengeVerification(challengeParticipation, challengeVerificationRequest)).getId();
    }

    @Transactional
    public void deleteChallengeVerification(MemberResponse memberResponse, Long challengeId, Long verificationId) {
        Member member = memberService.findMemberByIdOrThrow(memberResponse.id());
        Challenge challenge = challengeService.findChallengeByIdOrThrow(challengeId);
        ChallengeVerification challengeVerification = findByIdAndChallengeOrThrow(verificationId, challenge);

        if (!challengeVerification.getChallengeParticipation().getMember().getId().equals(member.getId())) {
            throw DeleteNotAuthorizedException.EXCEPTION;
        }

        challengeVerificationRepository.delete(challengeVerification);

        // S3에 저장된 이미지 삭제
        s3Service.deleteObject(challengeVerification.getImageKey());
    }

    private ChallengeVerificationResponse toChallengeVerificationResponse(ChallengeVerification challengeVerification) {
        return new ChallengeVerificationResponse(
                challengeVerification.getId(),
                challengeVerification.getChallengeParticipation().getId(),
                toAuthorResponse(challengeVerification.getChallengeParticipation().getMember()),
                challengeVerification.getTitle(),
                challengeVerification.getImageKey(),
                s3Service.getDownloadUrl(challengeVerification.getImageKey()).presignedUrl(),
                challengeVerification.getContent(),
                challengeVerification.getCreatedAt()
        );
    }

    private AuthorResponse toAuthorResponse(Member member) {
        return new AuthorResponse(
                member.getId(),
                member.getNickname(),
                member.getImageKey(),
                s3Service.getDownloadUrl(member.getImageKey()).presignedUrl()
        );
    }

    private ChallengeVerification toChallengeVerification(ChallengeParticipation challengeParticipation,
            ChallengeVerificationRequest challengeVerificationRequest) {
        return new ChallengeVerification(
                challengeVerificationRequest.title(),
                challengeVerificationRequest.imageKey(),
                challengeVerificationRequest.content(),
                challengeParticipation
        );
    }
}
