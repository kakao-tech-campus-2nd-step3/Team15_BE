package kakao.rebit.member.fixture.values;

import kakao.rebit.member.entity.Role;

public record MemberDefaultValues(
        Long id,
        String nickname,
        String imageKey,
        String bio,
        String email,
        Role role,
        Integer points,
        String kakaoToken,
        String coverImageKey
) {

    public static final MemberDefaultValues INSTANCE = new MemberDefaultValues(
            1L,
            "테스트용 닉네임",
            "default-image-key",
            "테스트용 자기소개",
            "test@email.com",
            Role.ROLE_USER,
            1000,
            "kakao-token",
            "default-cover-image-key"
    );
}
