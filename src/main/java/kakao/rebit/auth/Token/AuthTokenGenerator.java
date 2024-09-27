package kakao.rebit.auth.Token;

import kakao.rebit.auth.jwt.JwtTokenProvider;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class AuthTokenGenerator {

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 3600000;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1209600000;

    private final JwtTokenProvider jwtTokenProvider;

    public AuthTokenGenerator(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthToken generate(String uid) {
        Date now = new Date();
        Date accessTokenExpiry = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpiry = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME);

        String accessToken = jwtTokenProvider.accessTokenGenerate(uid, accessTokenExpiry);
        String refreshToken = jwtTokenProvider.refreshTokenGenerate(refreshTokenExpiry);

        return AuthToken.of(accessToken, refreshToken, "Bearer", ACCESS_TOKEN_EXPIRE_TIME / 1000);
    }
}
