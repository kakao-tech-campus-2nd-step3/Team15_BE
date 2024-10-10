package kakao.rebit.member.service;

import java.util.List;
import java.util.stream.Collectors;
import kakao.rebit.member.dto.MemberRequest;
import kakao.rebit.member.dto.MemberResponse;
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
    public List<MemberResponse> getAllMemberResponses() {
        return memberRepository.findAll().stream()
            .map(MemberResponse::convertFromEntity)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MemberResponse getMemberResponseById(Long memberId) {
        Member member = findMemberByIdOrThrow(memberId);
        return MemberResponse.convertFromEntity(member);
    }

    @Transactional(readOnly = true)
    public MemberResponse getMemberResponseByEmail(String email) {
        Member member = findMemberByEmailOrThrow(email);
        return MemberResponse.convertFromEntity(member);
    }

    @Transactional(readOnly = true)
    public Member findMemberByIdOrThrow(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));
    }

    @Transactional(readOnly = true)
    public Member findMemberByEmailOrThrow(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Member with email " + email + " not found"));
    }

    @Transactional
    public MemberResponse updateMember(Long memberId, MemberRequest memberRequest) {
        Member member = findMemberByIdOrThrow(memberId);
        member.updateProfile(memberRequest.nickname(), memberRequest.bio(), memberRequest.imageUrl());
        member.addPoints(memberRequest.point());
        memberRepository.save(member);
        return MemberResponse.convertFromEntity(member);
    }

    @Transactional
    public MemberResponse updateMyMember(String email, MemberRequest memberRequest) {
        Member member = findMemberByEmailOrThrow(email);
        member.updateProfile(memberRequest.nickname(), memberRequest.bio(), memberRequest.imageUrl());
        memberRepository.save(member);
        return MemberResponse.convertFromEntity(member);
    }

    @Transactional
    public void deleteMember(Long memberId) {
        Member member = findMemberByIdOrThrow(memberId);
        memberRepository.delete(member);
    }
}
