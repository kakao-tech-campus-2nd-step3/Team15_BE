package kakao.rebit.member.service;

import kakao.rebit.member.dto.MemberRequest;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
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
    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(
                () -> new IllegalArgumentException("Member with email " + email + " not found"));
    }

    @Transactional
    public Member updateMember(Long memberId, MemberRequest memberRequest) {
        Member member = findMemberByIdOrThrow(memberId);
        member.setNickname(memberRequest.getNickname());
        member.setBio(memberRequest.getBio());
        member.setImageUrl(memberRequest.getImageUrl());
        member.setPoint(memberRequest.getPoint());
        return memberRepository.save(member);
    }

    @Transactional
    public Member updateMyMember(String email, MemberRequest memberRequest) {
        Member member = findMemberByEmail(email);
        member.setNickname(memberRequest.getNickname());
        member.setBio(memberRequest.getBio());
        member.setImageUrl(memberRequest.getImageUrl());
        return memberRepository.save(member);
    }

    @Transactional
    public void deleteMember(Long memberId) {
        Member member = findMemberByIdOrThrow(memberId);
        memberRepository.delete(member);
    }
}
