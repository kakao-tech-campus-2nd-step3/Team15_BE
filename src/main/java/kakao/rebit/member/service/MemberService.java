package kakao.rebit.member.service;

import java.util.List;
import kakao.rebit.member.dto.MemberRequest;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 포인트 조회
    @Transactional(readOnly = true)
    public Integer getPoints(String email) {
        Member member = findMemberByEmailOrThrow(email);
        return member.getPoints();
    }

    // 포인트 충전
    @Transactional
    public void chargePoints(String email, Integer pointsToAdd) {
        Member member = findMemberByEmailOrThrow(email);
        member.addPoints(pointsToAdd);
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Member findMemberByIdOrThrow(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));
    }

    @Transactional(readOnly = true)
    public Member findMemberByEmailOrThrow(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(
                () -> new IllegalArgumentException("Member with email " + email + " not found"));
    }

    @Transactional
    public Member updateMember(Long memberId, MemberRequest memberRequest) {
        Member member = findMemberByIdOrThrow(memberId);
        member.updateProfile(memberRequest.nickname(), memberRequest.bio(), memberRequest.imageUrl());
        member.addPoints(memberRequest.point());
        return memberRepository.save(member);
    }

    @Transactional
    public Member updateMyMember(String email, MemberRequest memberRequest) {
        Member member = findMemberByEmailOrThrow(email);
        member.updateProfile(memberRequest.nickname(), memberRequest.bio(), memberRequest.imageUrl());
        return memberRepository.save(member);
    }

    @Transactional
    public void deleteMember(Long memberId) {
        Member member = findMemberByIdOrThrow(memberId);
        memberRepository.delete(member);
    }
}
