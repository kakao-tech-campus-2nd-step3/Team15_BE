package kakao.rebit.member.service;

import java.util.List;
import java.util.stream.Collectors;

import kakao.rebit.member.dto.AdminMemberRequest;
import kakao.rebit.challenge.repository.ChallengeParticipationRepository;
import kakao.rebit.diary.repository.DiaryRepository;
import kakao.rebit.feed.repository.FeedRepository;
import kakao.rebit.member.dto.MemberActivitySummaryResponse;
import kakao.rebit.member.dto.MemberProfileResponse;
import kakao.rebit.member.dto.MemberRequest;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.exception.MemberNotFoundException;
import kakao.rebit.member.mapper.MemberMapper;
import kakao.rebit.member.repository.MemberRepository;
import kakao.rebit.s3.service.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {
    private static final String DEFAULT_PROFILE_IMAGE_KEY = "member/default_image";
    private static final String DEFAULT_COVER_IMAGE_KEY = "cover/default_image";

    private final MemberRepository memberRepository;
    private final S3Service s3Service;
    private final MemberMapper memberMapper;
    private final ChallengeParticipationRepository challengeParticipationRepository;
    private final FeedRepository feedRepository;
    private final DiaryRepository diaryRepository;

    public MemberService(MemberRepository memberRepository, S3Service s3Service, MemberMapper memberMapper,
            ChallengeParticipationRepository challengeParticipationRepository,
            FeedRepository feedRepository,
            DiaryRepository diaryRepository) {
        this.memberRepository = memberRepository;
        this.s3Service = s3Service;
        this.memberMapper = memberMapper;
        this.challengeParticipationRepository = challengeParticipationRepository;
        this.feedRepository = feedRepository;
        this.diaryRepository = diaryRepository;
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
    public List<MemberProfileResponse> getAllMemberResponses() {
        return memberRepository.findAll().stream()
                .map(memberMapper::toMemberProfileResponse)
                .toList();
    }

    // ID로 특정 회원 정보 조회
    @Transactional(readOnly = true)
    public MemberProfileResponse getMemberResponseById(Long memberId) {
        Member member = findMemberByIdOrThrow(memberId);
        return memberMapper.toMemberProfileResponse(member);
    }

    // 이메일로 특정 회원 정보 조회
    @Transactional(readOnly = true)
    public MemberProfileResponse getMemberResponseByEmail(String email) {
        Member member = findMemberByEmailOrThrow(email);
        return memberMapper.toMemberProfileResponse(member);
    }

    // ID로 회원 조회
    @Transactional(readOnly = true)
    public Member findMemberByIdOrThrow(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.EXCEPTION);
    }

    // 이메일로 회원 조회
    @Transactional(readOnly = true)
    public Member findMemberByEmailOrThrow(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> MemberNotFoundException.EXCEPTION);
    }

    // 회원 정보 업데이트
    @Transactional
    public void updateMember(Long memberId, AdminMemberRequest adminMemberRequest) {
        Member member = findMemberByIdOrThrow(memberId);
        member.updateNicknameAndBio(adminMemberRequest.nickname(), adminMemberRequest.bio());
        member.addPoints(adminMemberRequest.point());
    }

    // 본인 정보 업데이트
    @Transactional
    public void updateMyMember(String email, MemberRequest memberRequest) {
        Member member = findMemberByEmailOrThrow(email);

        String preImageKey = member.getImageKey();
        String preCoverImage = member.getCoverImageKey();
        member.changeImageKey(memberRequest.imageKey());
        member.changeCoverImageKey(memberRequest.coverImageKey());

        member.updateNicknameAndBio(memberRequest.nickname(), memberRequest.bio());

        // imageKey가 변경된 경우, S3에 기존 이미지 삭제
        if (!preImageKey.equals(DEFAULT_PROFILE_IMAGE_KEY) && member.isImageKeyUpdated(preImageKey)) {
            s3Service.deleteObject(preImageKey);
        }

        if (!preCoverImage.equals(DEFAULT_COVER_IMAGE_KEY) && member.isCoverImageChanged(preCoverImage)){
            s3Service.deleteObject(preCoverImage);
        }
    }

    // 회원 삭제
    @Transactional
    public void deleteMember(Long memberId) {
        Member member = findMemberByIdOrThrow(memberId);
        memberRepository.delete(member);

        // S3에 저장된 이미지 삭제
        s3Service.deleteObject(member.getImageKey());
        s3Service.deleteObject(member.getCoverImageKey());
    }

    public MemberActivitySummaryResponse getMemberActivitySummary(String email) {
        Member member = findMemberByEmailOrThrow(email);

        long challengeCount = challengeParticipationRepository.countByMember(member);
        long feedCount = feedRepository.countByMember(member);
        long diaryCount = diaryRepository.countByMember(member);

        return new MemberActivitySummaryResponse(challengeCount, feedCount, diaryCount);
    }
}
