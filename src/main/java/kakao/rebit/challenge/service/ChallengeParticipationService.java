package kakao.rebit.challenge.service;

import kakao.rebit.challenge.dto.ChallengeParticipationRequest;
import kakao.rebit.challenge.dto.ChallengeParticipationMemberResponse;
import kakao.rebit.challenge.entity.Challenge;
import kakao.rebit.challenge.entity.ChallengeParticipation;
import kakao.rebit.challenge.repository.ChallengeParticipationRepository;
import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChallengeParticipationService {

    private final MemberService memberService;
    private final ChallengeService challengeService;
    private final ChallengeParticipationRepository challengeParticipationRepository;

    public ChallengeParticipationService(MemberService memberService, ChallengeService challengeService,
            ChallengeParticipationRepository challengeParticipationRepository) {
        this.memberService = memberService;
        this.challengeService = challengeService;
        this.challengeParticipationRepository = challengeParticipationRepository;
    }

    @Transactional(readOnly = true)
    public Page<ChallengeParticipationMemberResponse> getChallengeParticipationsById(Long challengeId, Pageable pageable) {
        Challenge challenge = challengeService.findChallengeByIdOrThrow(challengeId);
        Page<ChallengeParticipation> challengeParticipants = challengeParticipationRepository.findAllByChallenge(challenge, pageable);
        return challengeParticipants.map(this::toParticipantMemberResponse);
    }

    @Transactional(readOnly = true)
    public ChallengeParticipationMemberResponse getChallengeParticipationById(Long participantId) {
        ChallengeParticipation challengeParticipation = findChallengeParticipationByIdOrThrow(participantId);
        return toParticipantMemberResponse(challengeParticipation);
    }

    private ChallengeParticipation findChallengeParticipationByIdOrThrow(Long participantId) {
        return challengeParticipationRepository.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 참여 정보입니다."));
    }

    @Transactional
    public Long createChallengeParticipation(MemberResponse memberResponse, Long challengeId, ChallengeParticipationRequest challengeParticipationRequest) {
        Member member = memberService.findMemberByIdOrThrow(memberResponse.id());
        Challenge challenge = challengeService.findChallengeByIdOrThrow(challengeId);
        Integer entryFee = challengeParticipationRequest.entryFee();

        validateChallengeParticipation(member, challenge, entryFee);

        ChallengeParticipation challengeParticipation = toChallengeParticipant(member, challenge, entryFee);
        challengeParticipationRepository.save(challengeParticipation);

        return challengeParticipation.getId();
    }

    private void validateChallengeParticipation(Member member, Challenge challenge, Integer entryFee) {
        if (challengeParticipationRepository.existsByMemberAndChallenge(member, challenge)) {
            throw new IllegalArgumentException("이미 참여한 챌린지입니다.");
        }

        if (entryFee > member.getPoint()) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }

        if (entryFee < challenge.getMinimumEntryFee()) {
            throw new IllegalArgumentException("최소 예치금보다 적게 입력하셨습니다.");
        }
    }

    @Transactional
    public void cancelParticipation(Long participantId) {
        ChallengeParticipation challengeParticipation = findChallengeParticipationByIdOrThrow(participantId);
        challengeParticipationRepository.delete(challengeParticipation);
    }

    private ChallengeParticipationMemberResponse toParticipantMemberResponse(ChallengeParticipation challengeParticipation) {
        Member member = challengeParticipation.getMember();
        return new ChallengeParticipationMemberResponse(
                member.getId(),
                member.getNickname(),
                member.getImageUrl(),
                challengeParticipation.getCreatedAt(),
                challengeParticipation.getEntryFee()
        );
    }

    private ChallengeParticipation toChallengeParticipant(Member member, Challenge challenge, Integer entryFee) {
        return new ChallengeParticipation(
                challenge,
                member,
                entryFee
        );
    }
}
