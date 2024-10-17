package kakao.rebit.member.mapper;

import kakao.rebit.member.dto.MemberProfileResponse;
import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.member.entity.Member;
import kakao.rebit.s3.service.S3Service;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    private final S3Service s3Service;

    public MemberMapper(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    public MemberResponse toMemberResponse(Member member) {
        return new MemberResponse(
            member.getId(),
            member.getNickname(),
            member.getImageKey(),
            member.getBio(),
            member.getEmail(),
            member.getRole(),
            member.getPoints()
        );
    }

    public MemberProfileResponse toMemberProfileResponse(Member member){
        return new MemberProfileResponse(
                member.getId(),
                member.getNickname(),
                member.getImageKey(),
                s3Service.getDownloadUrl(member.getImageKey()).presignedUrl(),
                member.getBio(),
                member.getEmail(),
                member.getRole(),
                member.getPoints()
        );
    }
}
