package kakao.rebit.challenge.service;

import java.time.LocalDateTime;
import kakao.rebit.challenge.dto.ChallengeRequest;
import kakao.rebit.challenge.dto.ChallengeResponse;
import kakao.rebit.challenge.dto.CreatorResponse;
import kakao.rebit.challenge.entity.Challenge;
import kakao.rebit.challenge.entity.HeadcountLimit;
import kakao.rebit.challenge.entity.Period;
import kakao.rebit.challenge.exception.challenge.DeleteNotAllowedException;
import kakao.rebit.challenge.exception.challenge.DeleteNotAuthorizedException;
import kakao.rebit.challenge.exception.challenge.ChallengeNotFoundException;
import kakao.rebit.challenge.repository.ChallengeRepository;
import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final MemberService memberService;

    public ChallengeService(ChallengeRepository challengeRepository, MemberService memberService) {
        this.challengeRepository = challengeRepository;
        this.memberService = memberService;
    }

    @Transactional(readOnly = true)
    public Page<ChallengeResponse> getChallenges(Pageable pageable) {
        Page<Challenge> challenges = challengeRepository.findAll(pageable);
        return challenges.map(this::toChallengeResponse);
    }

    @Transactional(readOnly = true)
    public ChallengeResponse getChallengeById(Long challengeId) {
        Challenge challenge = findChallengeByIdOrThrow(challengeId);
        return toChallengeResponse(challenge);
    }

    @Transactional(readOnly = true)
    public Challenge findChallengeByIdOrThrow(Long challengeId) {
        return challengeRepository.findById(challengeId)
                .orElseThrow(() -> ChallengeNotFoundException.EXCEPTION);
    }

    @Transactional
    public Long createChallenge(MemberResponse memberResponse, ChallengeRequest challengeRequest) {
        Member member = memberService.findMemberByIdOrThrow(memberResponse.id());
        Challenge challenge = toChallenge(member, challengeRequest);
        return challengeRepository.save(challenge).getId();
    }

    @Transactional
    public void deleteChallengeById(MemberResponse memberResponse, Long challengeId) {
        Member member = memberService.findMemberByIdOrThrow(memberResponse.id());
        Challenge challenge = findChallengeByIdOrThrow(challengeId);

        if (!challenge.isHostedBy(member)) {
            throw DeleteNotAuthorizedException.EXCEPTION;
        }

        if (challenge.canBeDeleted(LocalDateTime.now())) {
            throw DeleteNotAllowedException.EXCEPTION;
        }

        challengeRepository.deleteById(challengeId);
    }

    private ChallengeResponse toChallengeResponse(Challenge challenge) {
        return new ChallengeResponse(
                challenge.getId(),
                toCreatorResponse(challenge.getMember()),
                challenge.getTitle(),
                challenge.getContent(),
                challenge.getImageUrl(),
                challenge.getType(),
                challenge.getMinimumEntryFee(),
                challenge.getRecruitmentPeriod().getStartDate(),
                challenge.getRecruitmentPeriod().getEndDate(),
                challenge.getChallengePeriod().getStartDate(),
                challenge.getChallengePeriod().getEndDate(),
                challenge.getHeadcountLimit().getMinHeadcount(),
                challenge.getHeadcountLimit().getMaxHeadcount(),
                challenge.getCreatedAt()
        );
    }

    private Challenge toChallenge(Member member, ChallengeRequest challengeRequest) {
        return new Challenge(
                member,
                challengeRequest.title(),
                challengeRequest.content(),
                challengeRequest.imageUrl(),
                challengeRequest.type(),
                challengeRequest.minimumEntryFee(),
                new Period(challengeRequest.recruitmentStartDate(), challengeRequest.recruitmentEndDate()),
                new Period(challengeRequest.challengeStartDate(), challengeRequest.challengeEndDate()),
                new HeadcountLimit(challengeRequest.minHeadcount(), challengeRequest.maxHeadcount())
        );
    }

    private CreatorResponse toCreatorResponse(Member member) {
        return new CreatorResponse(
                member.getId(),
                member.getNickname(),
                member.getImageUrl()
        );
    }
}
