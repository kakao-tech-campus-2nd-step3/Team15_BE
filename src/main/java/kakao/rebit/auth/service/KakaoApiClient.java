package kakao.rebit.auth.service;

import java.util.Objects;
import kakao.rebit.auth.dto.KakaoToken;
import kakao.rebit.auth.dto.KakaoUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoApiClient {

    private static final String BEARER_PREFIX = "Bearer ";

    @Value("${OAUTH_KAKAO_CLIENT_ID}")
    private String clientId;

    @Value("${OAUTH_KAKAO_REDIRECT_URI}")
    private String redirectUri;

    @Value("${OAUTH_KAKAO_AUTH_URL}")
    private String kakaoAuthUrl;

    @Value("${OAUTH_KAKAO_API_URL}")
    private String kakaoApiUrl;

    private final RestClient restClient;

    @Autowired
    public KakaoApiClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public String getAccessToken(String code) {
        MultiValueMap<String, String> body = createTokenParams(code);

        ResponseEntity<KakaoToken> response = restClient.post()
                .uri(kakaoAuthUrl + "/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toEntity(KakaoToken.class);

        KakaoToken tokens = Objects.requireNonNull(response.getBody());
        return tokens.getAccessToken();
    }

    private MultiValueMap<String, String> createTokenParams(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        return params;
    }

    public KakaoUserInfo getUserInfo(String accessToken) {
        ResponseEntity<KakaoUserInfo> response = restClient.get()
                .uri(kakaoApiUrl + "/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(KakaoUserInfo.class);

        KakaoUserInfo userInfo = response.getBody();
        if (userInfo == null) {
            throw new RuntimeException("사용자 정보가 비어 있습니다.");
        }

        return userInfo;
    }
}
