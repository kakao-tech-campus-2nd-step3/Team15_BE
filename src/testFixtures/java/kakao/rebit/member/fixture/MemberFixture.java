package kakao.rebit.member.fixture;

import static java.util.UUID.randomUUID;

import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.entity.Role;
import kakao.rebit.member.fixture.values.MemberDefaultValues;

public class MemberFixture {

    public static Member createRandomEmail() {
        MemberDefaultValues defaults = MemberDefaultValues.INSTANCE;
        return new Member(
                defaults.nickname(),
                defaults.imageKey(),
                defaults.bio(),
                randomUUID() + "@test.com",
                Role.ROLE_ADMIN,
                defaults.points(),
                defaults.kakaoToken()
        );
    }

    public static Member createDefault() {
        return createWithRole(Role.ROLE_USER);
    }

    public static Member createUser() {
        return createWithRole(Role.ROLE_USER);
    }

    public static Member createAdmin() {
        return createWithRole(Role.ROLE_ADMIN);
    }

    public static Member createCustomUser(String nickname) {
        MemberDefaultValues defaults = MemberDefaultValues.INSTANCE;
        return new Member(
                nickname,
                defaults.imageKey(),
                defaults.bio(),
                defaults.email(),
                Role.ROLE_USER,
                defaults.points(),
                defaults.kakaoToken()
        );
    }

    public static Member createWithRole(Role role) {
        MemberDefaultValues defaults = MemberDefaultValues.INSTANCE;
        return new Member(
                defaults.nickname(),
                defaults.imageKey(),
                defaults.bio(),
                defaults.email(),
                role,
                defaults.points(),
                defaults.kakaoToken()
        );
    }

    public static Member createWithPoints(Integer points) {
        MemberDefaultValues defaults = MemberDefaultValues.INSTANCE;
        return new Member(
                defaults.nickname(),
                defaults.imageKey(),
                defaults.bio(),
                defaults.email(),
                Role.ROLE_USER,
                points,
                defaults.kakaoToken()
        );
    }

    public static MemberResponse toMemberResponse(Member member) {
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
}
