package kakao.rebit.auth.dto;

import kakao.rebit.auth.Token.AuthToken;

public class LoginResponse {

    private AuthToken token;

    public LoginResponse() {
    }

    public LoginResponse(AuthToken token) {
        this.token = token;
    }

    public AuthToken getToken() {
        return token;
    }
}
