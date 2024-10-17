package kakao.rebit.auth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoUserInfo(Long id, KakaoAccount kakaoAccount, Properties properties) {
    public record KakaoAccount(String email) {}

    public record Properties(
            String nickname,
            String profile_image
    ) {}
}
