package kakao.rebit.member.dto;

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

}
