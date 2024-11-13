package kakao.rebit.wishlist.service;

import kakao.rebit.challenge.entity.Challenge;
import kakao.rebit.challenge.exception.challenge.ChallengeNotFoundException;
import kakao.rebit.challenge.repository.ChallengeRepository;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.exception.MemberNotFoundException;
import kakao.rebit.member.repository.MemberRepository;
import kakao.rebit.member.service.MemberService;
import kakao.rebit.challenge.service.ChallengeService;
import kakao.rebit.wishlist.dto.response.ChallengeWishlistResponse;
import kakao.rebit.wishlist.entity.ChallengeWishlist;
import kakao.rebit.wishlist.exception.AlreadyInWishlistException;
import kakao.rebit.wishlist.repository.ChallengeWishlistRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChallengeWishlistService {

    private final ChallengeWishlistRepository challengeWishlistRepository;
    private final MemberRepository memberRepository;
    private final ChallengeRepository challengeRepository;
    private final MemberService memberService;
    private final ChallengeService challengeService;

    public ChallengeWishlistService(ChallengeWishlistRepository challengeWishlistRepository,
            MemberRepository memberRepository, ChallengeRepository challengeRepository, MemberService memberService,
            ChallengeService challengeService) {
        this.challengeWishlistRepository = challengeWishlistRepository;
        this.memberRepository = memberRepository;
        this.challengeRepository = challengeRepository;
        this.memberService = memberService;
        this.challengeService = challengeService;
    }

    @Transactional(readOnly = true)
    public Page<ChallengeWishlistResponse> getChallengeWishlist(Long memberId, Pageable pageable) {
        Page<ChallengeWishlist> challengeWishlists = challengeWishlistRepository.findByMemberId(memberId, pageable);

        return challengeWishlists.map(challengeWishlist -> {
            boolean isWishlisted = true;
            return new ChallengeWishlistResponse(
                    challengeWishlist.getChallenge().getId(),
                    isWishlisted
            );
        });
    }

    @Transactional
    public void addChallengeWishlist(Long memberId, Long challengeId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.EXCEPTION);
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> ChallengeNotFoundException.EXCEPTION);

        // 이미 위시리스트에 존재하는 경우 예외 던지기
        if (challengeWishlistRepository.existsByMemberAndChallenge(member, challenge)) {
            throw AlreadyInWishlistException.EXCEPTION;
        }

        challengeWishlistRepository.save(new ChallengeWishlist(member, challenge));
    }

    @Transactional
    public void deleteChallengeWishlist(Long memberId, Long challengeId) {
        ChallengeWishlist challengeWishlist = challengeWishlistRepository
                .findByMemberAndChallengeOrThrow(memberService.findMemberByIdOrThrow(memberId),
                        challengeService.findChallengeByIdOrThrow(challengeId));

        challengeWishlistRepository.delete(challengeWishlist);
    }
}
