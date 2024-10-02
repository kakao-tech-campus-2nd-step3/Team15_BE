package kakao.rebit.challenge.service;

import kakao.rebit.challenge.dto.ChallengeParticipationRequest;
import kakao.rebit.challenge.dto.ChallengeParticipationMemberResponse;
import kakao.rebit.challenge.entity.Challenge;
import kakao.rebit.challenge.entity.ChallengeParticipant;
import kakao.rebit.challenge.repository.ChallengeParticipantRepository;
import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChallengeParticipantService {

    private final MemberService memberService;
    private final ChallengeService challengeService;
    private final ChallengeParticipantRepository challengeParticipantRepository;

    public ChallengeParticipantService(MemberService memberService, ChallengeService challengeService,
            ChallengeParticipantRepository challengeParticipantRepository) {
        this.memberService = memberService;
        this.challengeService = challengeService;
        this.challengeParticipantRepository = challengeParticipantRepository;
    }

    @Transactional(readOnly = true)
    public Page<ChallengeParticipationMemberResponse> getChallengeParticipantsById(Long challengeId, Pageable pageable) {
        Challenge challenge = challengeService.findChallengeByIdOrThrow(challengeId);
        Page<ChallengeParticipant> challengeParticipants = challengeParticipantRepository.findAllByChallenge(challenge, pageable);
        return challengeParticipants.map(this::toParticipantMemberResponse);
    }

    @Transactional(readOnly = true)
    public ChallengeParticipationMemberResponse getChallengeParticipantById(Long participantId) {
        ChallengeParticipant challengeParticipant = findChallengeParticipantByIdOrThrow(participantId);
        return toParticipantMemberResponse(challengeParticipant);
    }

    private ChallengeParticipant findChallengeParticipantByIdOrThrow(Long participantId) {
        return challengeParticipantRepository.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 참여 정보입니다."));
    }

    @Transactional
    public Long createChallengeParticipation(MemberResponse memberResponse, Long challengeId, ChallengeParticipationRequest challengeParticipationRequest) {
        Member member = memberService.findMemberByIdOrThrow(memberResponse.id());
        Challenge challenge = challengeService.findChallengeByIdOrThrow(challengeId);
        Integer entryFee = challengeParticipationRequest.entryFee();

        validateChallengeParticipation(member, challenge, entryFee);

        ChallengeParticipant challengeParticipant = toChallengeParticipant(member, challenge, entryFee);
        challengeParticipantRepository.save(challengeParticipant);

        return challengeParticipant.getId();
    }

    private void validateChallengeParticipation(Member member, Challenge challenge, Integer entryFee) {
        if (challengeParticipantRepository.existsByMemberAndChallenge(member, challenge)) {
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
        ChallengeParticipant challengeParticipant = findChallengeParticipantByIdOrThrow(participantId);
        challengeParticipantRepository.delete(challengeParticipant);
    }

    private ChallengeParticipationMemberResponse toParticipantMemberResponse(ChallengeParticipant challengeParticipant) {
        Member member = challengeParticipant.getMember();
        return new ChallengeParticipationMemberResponse(
                member.getId(),
                member.getNickname(),
                member.getImageUrl(),
                challengeParticipant.getCreatedAt(),
                challengeParticipant.getEntryFee()
        );
    }

    private ChallengeParticipant toChallengeParticipant(Member member, Challenge challenge, Integer entryFee) {
        return new ChallengeParticipant(
                challenge,
                member,
                entryFee
        );
    }
}
