package kakao.rebit.auth.service;

import jakarta.transaction.Transactional;
import kakao.rebit.auth.token.AuthTokenGenerator;
import kakao.rebit.auth.dto.KakaoUserInfo;
import kakao.rebit.auth.dto.LoginResponse;
import kakao.rebit.auth.token.AuthToken;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.entity.Role;
import kakao.rebit.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        // 3. 회원 조회 또는 생성 메서드 호출
        Member member = findOrCreateMemberByEmail(userInfo, accessToken);

        // 4. JWT 토큰 생성
        AuthToken tokens = generateAuthToken(member);

        return new LoginResponse(tokens);
    }

    @Transactional
    public Member findOrCreateMemberByEmail(KakaoUserInfo userInfo, String accessToken) {
        String email = userInfo.kakaoAccount().email();

        // 이메일로 회원을 조회하고 없으면 새로운 회원 생성
        return memberRepository.findByEmail(email)
            .orElseGet(() -> newMember(userInfo, accessToken, email));
    }

    private Member newMember(KakaoUserInfo userInfo, String accessToken, String email) {
        // 사용자 정보를 바탕으로 새로운 회원 생성
        String nickname = userInfo.properties().nickname();
        Member newMember = new Member(nickname, "", "", email, Role.ROLE_USER, 0, accessToken);
        return memberRepository.save(newMember);
    }

    private AuthToken generateAuthToken(Member member) {
        return authTokensGenerator.generate(
            member.getId().toString(),
            member.getEmail(),
            member.getRole().name()
        );
    }
}
