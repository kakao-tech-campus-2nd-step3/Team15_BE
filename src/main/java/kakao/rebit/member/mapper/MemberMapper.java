package kakao.rebit.member.mapper;

import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.member.entity.Member;

public class MemberMapper {

    public static MemberResponse toDto(Member member) {
        return new MemberResponse(
            member.getId(),
            member.getNickname(),
            member.getImageUrl(),
            member.getBio(),
            member.getEmail(),
            member.getRole(),
            member.getPoints()
        );
    }
}
