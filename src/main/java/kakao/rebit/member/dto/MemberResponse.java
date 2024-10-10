package kakao.rebit.member.dto;

import kakao.rebit.member.entity.Member;
import kakao.rebit.member.entity.Role;

public record MemberResponse(
    Long id,
    String nickname,
    String imageUrl,
    String bio,
    String email,
    Role role,
    Integer point
) {
    public static MemberResponse convertFromEntity(Member member) {
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
