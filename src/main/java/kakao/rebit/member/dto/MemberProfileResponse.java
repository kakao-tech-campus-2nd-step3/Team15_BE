package kakao.rebit.member.dto;

import kakao.rebit.member.entity.Role;

public record MemberProfileResponse (
        Long id,
        String nickname,
        String imageKey,
        String presignedUrl,
        String bio,
        String email,
        Role role,
        Integer point
){

}
