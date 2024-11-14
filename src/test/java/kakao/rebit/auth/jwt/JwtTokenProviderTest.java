package kakao.rebit.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Date;
import javax.crypto.SecretKey;
import kakao.rebit.auth.jwt.exception.ExpiredTokenException;
import kakao.rebit.auth.jwt.exception.InvalidTokenException;
import kakao.rebit.auth.jwt.exception.MissingTokenException;
import kakao.rebit.auth.jwt.exception.SignatureValidationFailedException;
import kakao.rebit.auth.jwt.exception.UnsupportedTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtTokenProvider 테스트")
class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private TokenBlacklistRepository tokenBlacklistRepository;

    private SecretKey key;
    private static final String SECRET = Base64.getEncoder().encodeToString("mysecretkeymysecretkeymysecretkeymysecretkey".getBytes());
    private static final String TEST_UID = "123";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_ROLE = "ROLE_USER";
    private static final long ONE_HOUR = 60 * 60 * 1000;

    @BeforeEach
    void setUp() {
        key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET));
        jwtTokenProvider = new JwtTokenProvider(SECRET, tokenBlacklistRepository);
    }


    @Test
    void 액세스_토큰_생성() {
        Date expiration = new Date((System.currentTimeMillis() + ONE_HOUR) / 1000 * 1000);
        String token = jwtTokenProvider.accessTokenGenerate(TEST_UID, TEST_EMAIL, TEST_ROLE, expiration);

        Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
        assertThat(claims.getSubject()).isEqualTo(TEST_UID);
        assertThat(claims.get("email")).isEqualTo(TEST_EMAIL);
        assertThat(claims.get("role")).isEqualTo(TEST_ROLE);
        assertThat(claims.getExpiration().getTime() / 1000).isEqualTo(expiration.getTime() / 1000); // 밀리초 제외 비교
    }

    @Test
    void 리프레시_토큰_생성() {
        Date expiration = new Date((System.currentTimeMillis() + ONE_HOUR) / 1000 * 1000);
        String token = jwtTokenProvider.refreshTokenGenerate(expiration);

        Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
        assertThat(claims.getExpiration().getTime() / 1000).isEqualTo(expiration.getTime() / 1000); // 초 단위로 비교
    }

    @Test
    void 토큰에서_이메일_추출() {
        Date expiration = new Date(System.currentTimeMillis() + ONE_HOUR);
        String token = jwtTokenProvider.accessTokenGenerate(TEST_UID, TEST_EMAIL, TEST_ROLE, expiration);

        String email = jwtTokenProvider.getEmailFromToken(token);
        assertThat(email).isEqualTo(TEST_EMAIL);
    }

    @Test
    void 토큰_유효성_검사_성공() {
        Date expiration = new Date(System.currentTimeMillis() + ONE_HOUR);
        String token = jwtTokenProvider.accessTokenGenerate(TEST_UID, TEST_EMAIL, TEST_ROLE, expiration);

        when(tokenBlacklistRepository.isBlacklisted(token)).thenReturn(false);

        assertThat(jwtTokenProvider.validateToken(token)).isTrue();
        verify(tokenBlacklistRepository).isBlacklisted(token);
    }

    @Test
    void 블랙리스트_토큰_유효성_검사_실패() {
        Date expiration = new Date(System.currentTimeMillis() + ONE_HOUR);
        String token = jwtTokenProvider.accessTokenGenerate(TEST_UID, TEST_EMAIL, TEST_ROLE, expiration);

        when(tokenBlacklistRepository.isBlacklisted(token)).thenReturn(true);

        assertThatThrownBy(() -> jwtTokenProvider.validateToken(token))
                .isInstanceOf(InvalidTokenException.class);
        verify(tokenBlacklistRepository).isBlacklisted(token);
    }

    @Test
    void 만료된_토큰_유효성_검사() {
        Date expiration = new Date(System.currentTimeMillis() - ONE_HOUR); // 이미 만료된 토큰
        String token = jwtTokenProvider.accessTokenGenerate(TEST_UID, TEST_EMAIL, TEST_ROLE, expiration);

        assertThatThrownBy(() -> jwtTokenProvider.validateToken(token))
                .isInstanceOf(ExpiredTokenException.class);
    }

    @Test
    void 토큰_서명_오류() {
        Date expiration = new Date(System.currentTimeMillis() + ONE_HOUR);
        String token = jwtTokenProvider.accessTokenGenerate(TEST_UID, TEST_EMAIL, TEST_ROLE, expiration);

        SecretKey invalidKey = Keys.hmacShaKeyFor("invalidsecretkeyinvalidsecretkeyinvalidsecretkey".getBytes());
        String forgedToken = Jwts.builder()
                .setSubject(TEST_UID)
                .claim("email", TEST_EMAIL)
                .claim("role", TEST_ROLE)
                .setExpiration(expiration)
                .signWith(invalidKey, SignatureAlgorithm.HS256)
                .compact();

        assertThatThrownBy(() -> jwtTokenProvider.validateToken(forgedToken))
                .isInstanceOf(SignatureValidationFailedException.class);
    }

    @Test
    void 토큰_블랙리스트_추가() {
        Date expiration = new Date(System.currentTimeMillis() + ONE_HOUR);
        String token = jwtTokenProvider.accessTokenGenerate(TEST_UID, TEST_EMAIL, TEST_ROLE, expiration);

        jwtTokenProvider.addToBlacklist(token);
        verify(tokenBlacklistRepository).addToBlacklist(eq(token), anyLong());
    }

    @Test
    void Bearer_접두사_없는_토큰_추출_오류() {
        String tokenWithoutPrefix = "invalidToken";

        assertThatThrownBy(() -> jwtTokenProvider.extractToken(tokenWithoutPrefix))
                .isInstanceOf(UnsupportedTokenException.class);
    }

    @Test
    void 널_토큰_추출_오류() {
        assertThatThrownBy(() -> jwtTokenProvider.extractToken(null))
                .isInstanceOf(MissingTokenException.class);
    }

    @Test
    void 올바른_Bearer_토큰_추출() {
        String token = "Bearer validToken";
        assertThat(jwtTokenProvider.extractToken(token)).isEqualTo("validToken");
    }
}
