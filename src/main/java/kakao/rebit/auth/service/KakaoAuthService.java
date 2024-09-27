package kakao.rebit.auth.service;

import kakao.rebit.auth.Token.AuthTokenGenerator;
import kakao.rebit.auth.dto.KakaoUserInfo;
import kakao.rebit.auth.dto.LoginResponse;
import kakao.rebit.auth.Token.AuthToken;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.entity.Role;
import kakao.rebit.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KakaoAuthService {

    private final KakaoApiClient kakaoApiClient;
    private final MemberRepository memberRepository;
    private final AuthTokenGenerator authTokensGenerator;

    @Autowired
    public KakaoAuthService(KakaoApiClient kakaoApiClient, MemberRepository memberRepository,
        AuthTokenGenerator authTokensGenerator) {
        this.kakaoApiClient = kakaoApiClient;
        this.memberRepository = memberRepository;
        this.authTokensGenerator = authTokensGenerator;
    }

    public LoginResponse kakaoLogin(String code) {
        // 1. 카카오로부터 액세스 토큰을 받음
        String accessToken = kakaoApiClient.getAccessToken(code);

        // 2. 받은 액세스 토큰으로 유저 정보를 가져옴
        KakaoUserInfo userInfo = kakaoApiClient.getUserInfo(accessToken);
        Long kakaoId = userInfo.id();
        String nickname = userInfo.properties().nickname();
        String email = userInfo.kakaoAccount().email();

        // 3. 카카오 이메일로 기존 유저 조회
        Optional<Member> memberOptional = memberRepository.findByEmail(email);

        Member member = memberOptional.orElseGet(() -> {
            // 회원이 없으면 회원가입 처리, 기본 Role은 ROLE_USER로 설정
            Member newMember = new Member(nickname, email,null, null, Role.ROLE_USER, null, accessToken);
            memberRepository.save(newMember);
            return newMember;
        });

        // 4. 자체 JWT 토큰 생성
        AuthToken tokens = authTokensGenerator.generate(member.getId().toString());

        // 5. 로그인 응답 반환
        return new LoginResponse(tokens);
    }
}
