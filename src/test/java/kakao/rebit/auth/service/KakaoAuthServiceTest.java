package kakao.rebit.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import kakao.rebit.auth.dto.KakaoUserInfo;
import kakao.rebit.auth.dto.LoginResponse;
import kakao.rebit.auth.event.RegisteredEvent;
import kakao.rebit.auth.jwt.JwtTokenProvider;
import kakao.rebit.auth.token.AuthToken;
import kakao.rebit.auth.token.AuthTokenGenerator;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.entity.Role;
import kakao.rebit.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("카카오 로그인-로그아웃 테스트")
class KakaoAuthServiceTest {

    @InjectMocks
    private KakaoAuthService kakaoAuthService;

    @Mock
    private KakaoApiClient kakaoApiClient;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AuthTokenGenerator authTokenGenerator;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private ApplicationEventPublisher publisher;

    private Member member;
    private AuthToken authToken;
    private static final String TEST_CODE = "test-code";
    private static final String TEST_ACCESS_TOKEN = "test-access-token";

    @BeforeEach
    void setUp() {
        member = mock(Member.class);
        authToken = new AuthToken("accessToken", "refreshToken", "Bearer", 3600L);
    }

    @Test
    void 카카오_로그인_성공() {
        // given
        given(member.getId()).willReturn(1L); // ID 값을 1로 모의 설정
        given(member.getEmail()).willReturn("test@example.com");
        given(member.getRole()).willReturn(Role.ROLE_USER);

        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(1L, new KakaoUserInfo.KakaoAccount("test@example.com"),
                new KakaoUserInfo.Properties("nickname", "profileImage"));
        given(kakaoApiClient.getAccessToken(TEST_CODE)).willReturn(TEST_ACCESS_TOKEN);
        given(kakaoApiClient.getUserInfo(TEST_ACCESS_TOKEN)).willReturn(kakaoUserInfo);
        given(memberRepository.findByEmail(kakaoUserInfo.kakaoAccount().email())).willReturn(Optional.empty());
        given(memberRepository.save(any(Member.class))).willReturn(member);
        given(authTokenGenerator.generate(String.valueOf(member.getId()), member.getEmail(), member.getRole().name())).willReturn(authToken);

        // when
        LoginResponse loginResponse = kakaoAuthService.kakaoLogin(TEST_CODE);

        // then
        assertThat(loginResponse).isNotNull();
        assertThat(loginResponse.getToken()).isEqualTo(authToken);
        verify(memberRepository).save(any(Member.class));

        // publisher가 정확한 이벤트를 발행했는지 검증
        ArgumentCaptor<RegisteredEvent> eventCaptor = ArgumentCaptor.forClass(RegisteredEvent.class);
        verify(publisher).publishEvent(eventCaptor.capture());

        RegisteredEvent capturedEvent = eventCaptor.getValue();
        assertThat(capturedEvent.email()).isEqualTo("test@example.com");
        assertThat(capturedEvent.profileImageUrl()).isEqualTo("profileImage");
    }

    @Test
    void 이미_등록된_회원의_카카오_로그인() {
        // given
        given(member.getId()).willReturn(1L); // ID 값을 1로 모의 설정
        given(member.getEmail()).willReturn("test@example.com");
        given(member.getRole()).willReturn(Role.ROLE_USER);

        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(1L, new KakaoUserInfo.KakaoAccount("test@example.com"),
                new KakaoUserInfo.Properties("nickname", "profileImage"));
        given(kakaoApiClient.getAccessToken(TEST_CODE)).willReturn(TEST_ACCESS_TOKEN);
        given(kakaoApiClient.getUserInfo(TEST_ACCESS_TOKEN)).willReturn(kakaoUserInfo);
        given(memberRepository.findByEmail(kakaoUserInfo.kakaoAccount().email())).willReturn(Optional.of(member));
        given(authTokenGenerator.generate(String.valueOf(member.getId()), member.getEmail(), member.getRole().name())).willReturn(authToken);

        // when
        LoginResponse loginResponse = kakaoAuthService.kakaoLogin(TEST_CODE);

        // then
        assertThat(loginResponse).isNotNull();
        assertThat(loginResponse.getToken()).isEqualTo(authToken);
        verify(memberRepository).findByEmail(kakaoUserInfo.kakaoAccount().email());
    }

    @Test
    void 카카오_로그아웃() {
        // given
        String jwtToken = "test-jwt-token";
        doNothing().when(jwtTokenProvider).addToBlacklist(jwtToken);
        doNothing().when(kakaoApiClient).logout();

        // when
        kakaoAuthService.kakaoLogout(jwtToken);

        // then
        verify(jwtTokenProvider).addToBlacklist(jwtToken);
        verify(kakaoApiClient).logout();
    }
}
