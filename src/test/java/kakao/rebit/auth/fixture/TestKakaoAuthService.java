package kakao.rebit.auth.fixture;

import kakao.rebit.auth.dto.LoginResponse;
import kakao.rebit.auth.service.KakaoAuthService;
import kakao.rebit.auth.token.AuthToken;
import kakao.rebit.auth.token.AuthTokenGenerator;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.fixture.MemberFixture;
import kakao.rebit.member.repository.MemberRepository;

public class TestKakaoAuthService extends KakaoAuthService {

    private final MemberRepository memberRepository;
    private final AuthTokenGenerator authTokenGenerator;

    public TestKakaoAuthService(MemberRepository memberRepository, AuthTokenGenerator authTokenGenerator) {
        super(null, memberRepository, authTokenGenerator, null, null);
        this.memberRepository = memberRepository;
        this.authTokenGenerator = authTokenGenerator;
    }

    @Override
    public LoginResponse kakaoLogin(String code) {
        Member member = MemberFixture.createDefault();
        memberRepository.save(member);

        AuthToken token = authTokenGenerator.generate(member.getId().toString(), member.getEmail(), member.getRole().toString());

        return new LoginResponse(token, member.getId());
    }

    @Override
    public void kakaoLogout(String accessToken) {
        // 테스트에서는 아무 동작도 하지 않음
    }
}
