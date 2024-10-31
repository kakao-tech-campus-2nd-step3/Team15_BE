package kakao.rebit.auth.service;

import jakarta.transaction.Transactional;
import kakao.rebit.auth.dto.KakaoUserInfo;
import kakao.rebit.auth.dto.LoginResponse;
import kakao.rebit.auth.jwt.JwtTokenProvider;
import kakao.rebit.auth.jwt.TokenBlacklistRepository;
import kakao.rebit.auth.token.AuthToken;
import kakao.rebit.auth.token.AuthTokenGenerator;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.repository.MemberRepository;
import kakao.rebit.s3.domain.S3Type;
import kakao.rebit.s3.dto.DownloadImageInfo;
import kakao.rebit.s3.dto.S3UploadKeyRequest;
import kakao.rebit.s3.service.S3Service;
import kakao.rebit.utils.file.FileUtil;
import kakao.rebit.utils.image.ImageDownloader;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class KakaoAuthService {

    private final KakaoApiClient kakaoApiClient;
    private final MemberRepository memberRepository;
    private final AuthTokenGenerator authTokensGenerator;
    private final S3Service s3Service;
    private final ImageDownloader imageDownloader;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlacklistRepository tokenBlacklistRepository;

    public KakaoAuthService(
        KakaoApiClient kakaoApiClient,
        MemberRepository memberRepository,
        AuthTokenGenerator authTokensGenerator,
        S3Service s3Service,
        ImageDownloader imageDownloader,
        JwtTokenProvider jwtTokenProvider,
        TokenBlacklistRepository tokenBlacklistRepository) {
        this.kakaoApiClient = kakaoApiClient;
        this.memberRepository = memberRepository;
        this.authTokensGenerator = authTokensGenerator;
        this.s3Service = s3Service;
        this.imageDownloader = imageDownloader;
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenBlacklistRepository = tokenBlacklistRepository;
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
                .orElseGet(() -> createNewMember(userInfo, accessToken));
    }

    private Member createNewMember(KakaoUserInfo userInfo, String accessToken) {
        // 사용자 정보를 바탕으로 새로운 회원 생성
        String nickname = userInfo.properties().nickname();
        String profileImageUrl = userInfo.properties().profileImage();
        String email = userInfo.kakaoAccount().email();

        S3UploadKeyRequest s3UploadKeyRequest = s3Service.createS3UploadKeyRequestFromTypeAndFilename(S3Type.MEMBER, FileUtil.getFilenameFromUrl(profileImageUrl)); // imageUrl로부터 imageKey 및 contentType 획득

        Member newMember = memberRepository.save(
                Member.init(nickname, s3UploadKeyRequest.imageKey(), email, accessToken)); // 새로운 멤버 생성 후 저장

        downloadImageAndUploadS3(profileImageUrl, s3UploadKeyRequest); // 카카오 프로필 이미지 다운 후 S3에 업로드

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
        // 현재 요청에서 Authorization 헤더에서 JWT 토큰을 추출
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = jwtTokenProvider.extractToken(request.getHeader("Authorization"));

        // JWT 토큰을 블랙리스트에 추가
        long expiration = jwtTokenProvider.getExpiration(token);
        tokenBlacklistRepository.addToBlacklist(token, expiration);

        // 카카오 API를 사용하여 카카오 로그아웃 수행
        kakaoApiClient.logout();
    }
}
