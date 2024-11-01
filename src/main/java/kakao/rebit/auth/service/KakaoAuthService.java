package kakao.rebit.auth.service;

import jakarta.transaction.Transactional;
import kakao.rebit.auth.dto.KakaoUserInfo;
import kakao.rebit.auth.dto.LoginResponse;
import kakao.rebit.auth.jwt.JwtTokenProvider;
import kakao.rebit.auth.jwt.TokenBlacklistRepository;
import kakao.rebit.auth.event.RegisteredEvent;
import kakao.rebit.auth.token.AuthToken;
import kakao.rebit.auth.token.AuthTokenGenerator;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.repository.MemberRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class KakaoAuthService {

    private static final String DEFAULT_PROFILE_IMAGE_KEY = "member/default_profile";
    private final KakaoApiClient kakaoApiClient;
    private final MemberRepository memberRepository;
    private final AuthTokenGenerator authTokensGenerator;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final ApplicationEventPublisher publisher;
  
    public KakaoAuthService(
        KakaoApiClient kakaoApiClient,
        MemberRepository memberRepository,
        AuthTokenGenerator authTokensGenerator,
        JwtTokenProvider jwtTokenProvider,
        TokenBlacklistRepository tokenBlacklistRepository,
        ApplicationEventPublisher publisher
    ) {
        this.kakaoApiClient = kakaoApiClient;
        this.memberRepository = memberRepository;
        this.authTokensGenerator = authTokensGenerator;
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenBlacklistRepository = tokenBlacklistRepository;
        this.publisher = publisher;
    }

    @Transactional
    public LoginResponse kakaoLogin(String code) {
        // 1. 카카오로부터 액세스 토큰을 받음
        String accessToken = kakaoApiClient.getAccessToken(code);

        // 2. 받은 액세스 토큰으로 유저 정보를 가져옴
        KakaoUserInfo userInfo = kakaoApiClient.getUserInfo(accessToken);

        // 3. 회원 조회 또는 생성 메서드 호출
        Member member = findOrCreateMemberByEmail(userInfo, accessToken);

        // 4. JWT 토큰 생성
        AuthToken tokens = generateAuthToken(member);

        return new LoginResponse(tokens, member.getId());
    }

    @Transactional
    public Member findOrCreateMemberByEmail(KakaoUserInfo userInfo, String accessToken) {
        String email = userInfo.kakaoAccount().email();

        // 이메일로 회원을 조회하고 없으면 새로운 회원 생성
        return memberRepository.findByEmail(email)
                .orElseGet(() -> createMember(userInfo, accessToken));
    }

    private Member createMember(KakaoUserInfo userInfo, String accessToken) {
        // 사용자 정보를 바탕으로 새로운 회원 생성
        String nickname = userInfo.properties().nickname();
        String profileImageUrl = userInfo.properties().profileImage();
        String email = userInfo.kakaoAccount().email();

        Member newMember = memberRepository.save(
                Member.init(nickname, DEFAULT_PROFILE_IMAGE_KEY, email, accessToken)); // 새로운 멤버 생성 후 저장

        publisher.publishEvent(RegisteredEvent.init(email, profileImageUrl));

        return newMember;
    }

    private AuthToken generateAuthToken(Member member) {
        return authTokensGenerator.generate(
                member.getId().toString(),
                member.getEmail(),
                member.getRole().name()
        );
    }

    private void downloadImageAndUploadS3(String imageUrl, S3UploadKeyRequest s3UploadKeyRequest) {
        DownloadImageInfo downloadImageInfo = imageDownloader.downloadImageFromUrl(imageUrl); // imageUrl로부터 이미지 가져오기
        s3Service.putObject(s3UploadKeyRequest, downloadImageInfo); // S3에 저장
    }

    public void kakaoLogout(String jwtToken) {
        // 전달받은 JWT 토큰을 블랙리스트에 추가
        String token = jwtTokenProvider.extractToken(jwtToken);
        jwtTokenProvider.addToBlacklist(token);

        // 카카오 API를 사용하여 카카오 로그아웃 수행
        kakaoApiClient.logout();
    }
}
