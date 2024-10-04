package kakao.rebit.auth.token;

public class AuthToken {

    private String accessToken;
    private String refreshToken;
    private String grantType;
    private Long expiresIn;

    public AuthToken(String accessToken, String refreshToken, String grantType, Long expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.grantType = grantType;
        this.expiresIn = expiresIn;
    }

    public static AuthToken of(String accessToken, String refreshToken, String grantType,
        Long expiresIn) {
        return new AuthToken(accessToken, refreshToken, grantType, expiresIn);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getGrantType() {
        return grantType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }
}
