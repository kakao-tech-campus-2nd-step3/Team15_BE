package kakao.rebit.wishlist.service;

import kakao.rebit.challenge.entity.Challenge;
import kakao.rebit.challenge.repository.ChallengeRepository;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.repository.MemberRepository;
import kakao.rebit.wishlist.entity.ChallengeWishlist;
import kakao.rebit.wishlist.repository.ChallengeWishlistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<Long> getChallengeWishlist(Long memberId) {
        return challengeWishlistRepository.findAll().stream()
            .filter(challengeWishlist -> challengeWishlist.getMember().getId().equals(memberId))
            .map(challengeWishlist -> challengeWishlist.getChallenge().getId())
            .collect(Collectors.toList());
    }

    @Transactional
    public void addChallengeWishlist(Long memberId, Long challengeId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member not found"));
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(() -> new IllegalArgumentException("Challenge not found"));
        challengeWishlistRepository.save(new ChallengeWishlist(member, challenge));
    }

    @Transactional
    public void deleteChallengeWishlist(Long memberId, Long challengeId) {
        challengeWishlistRepository.findAll().stream()
            .filter(challengeWishlist -> challengeWishlist.getMember().getId().equals(memberId) && challengeWishlist.getChallenge().getId().equals(challengeId))
            .findFirst()
            .ifPresent(challengeWishlistRepository::delete);
    }
}
