package kakao.rebit.auth.dto;

import kakao.rebit.auth.token.AuthToken;

public class LoginResponse {

    private AuthToken token;
    private Long memberId;

    public LoginResponse() {
    }

    public LoginResponse(AuthToken token, Long memberId) {
        this.token = token;
        this.memberId = memberId;
    }

    public AuthToken getToken() {
        return token;
    }

    public Long getMemberId() {
        return memberId;
    }
}
