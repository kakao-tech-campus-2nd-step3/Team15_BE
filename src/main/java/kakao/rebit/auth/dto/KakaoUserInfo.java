package kakao.rebit.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUserInfo(
    @JsonProperty("id") Long id,
    @JsonProperty("kakao_account") KakaoAccount kakaoAccount,
    @JsonProperty("properties") Properties properties
) {
    public record KakaoAccount(
        @JsonProperty("email") String email
    ) {}

    public record Properties(
        @JsonProperty("nickname") String nickname
    ) {}
}
