package kakao.rebit.member.service;

import java.util.List;
import java.util.stream.Collectors;
import kakao.rebit.member.dto.MemberRequest;
import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.mapper.MemberMapper;
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

    // 모든 회원 정보 조회
    @Transactional(readOnly = true)
    public List<MemberResponse> getAllMemberResponses() {
        return memberRepository.findAll().stream()
            .map(MemberMapper::toDto)
            .collect(Collectors.toList());
    }

    // ID로 특정 회원 정보 조회
    @Transactional(readOnly = true)
    public MemberResponse getMemberResponseById(Long memberId) {
        Member member = findMemberByIdOrThrow(memberId);
        return MemberMapper.toDto(member);
    }

    // 이메일로 특정 회원 정보 조회
    @Transactional(readOnly = true)
    public MemberResponse getMemberResponseByEmail(String email) {
        Member member = findMemberByEmailOrThrow(email);
        return MemberMapper.toDto(member);
    }

    // ID로 회원 조회
    @Transactional(readOnly = true)
    public Member findMemberByIdOrThrow(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
    }

    // 이메일로 회원 조회
    @Transactional(readOnly = true)
    public Member findMemberByEmailOrThrow(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("이메일 " + email + "에 해당하는 회원을 찾을 수 없습니다."));
    }

    // 회원 정보 업데이트
    @Transactional
    public MemberResponse updateMember(Long memberId, MemberRequest memberRequest) {
        Member member = findMemberByIdOrThrow(memberId);
        member.updateProfile(memberRequest.nickname(), memberRequest.bio(), memberRequest.imageUrl());
        member.addPoints(memberRequest.point());
        memberRepository.save(member);
        return MemberMapper.toDto(member);
    }

    // 본인 정보 업데이트
    @Transactional
    public MemberResponse updateMyMember(String email, MemberRequest memberRequest) {
        Member member = findMemberByEmailOrThrow(email);
        member.updateProfile(memberRequest.nickname(), memberRequest.bio(), memberRequest.imageUrl());
        memberRepository.save(member);
        return MemberMapper.toDto(member);
    }

    // 회원 삭제
    @Transactional
    public void deleteMember(Long memberId) {
        Member member = findMemberByIdOrThrow(memberId);
        memberRepository.delete(member);
    }
}
