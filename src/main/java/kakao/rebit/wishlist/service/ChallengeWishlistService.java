package kakao.rebit.wishlist.service;

import kakao.rebit.challenge.entity.Challenge;
import kakao.rebit.challenge.repository.ChallengeRepository;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.repository.MemberRepository;
import kakao.rebit.wishlist.entity.ChallengeWishlist;
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

    public ChallengeWishlistService(ChallengeWishlistRepository challengeWishlistRepository, MemberRepository memberRepository, ChallengeRepository challengeRepository) {
        this.challengeWishlistRepository = challengeWishlistRepository;
        this.memberRepository = memberRepository;
        this.challengeRepository = challengeRepository;
    }

    @Transactional(readOnly = true)
    public Page<Long> getChallengeWishlist(Long memberId, Pageable pageable) {
        return challengeWishlistRepository.findByMemberId(memberId, pageable)
            .map(challengeWishlist -> challengeWishlist.getChallenge().getId());
    }

    @Transactional
    public void addChallengeWishlist(Long memberId, Long challengeId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        Challenge challenge = challengeRepository.findById(challengeId)
            .orElseThrow(() -> new IllegalArgumentException("Challenge not found"));
        challengeWishlistRepository.save(new ChallengeWishlist(member, challenge));
    }

    @Transactional
    public void deleteChallengeWishlist(Long memberId, Long challengeId) {
        challengeWishlistRepository.findAll().stream()
            .filter(challengeWishlist -> challengeWishlist.getMember().getId().equals(memberId)
                && challengeWishlist.getChallenge().getId().equals(challengeId))
            .findFirst()
            .ifPresent(challengeWishlistRepository::delete);
    }
}
