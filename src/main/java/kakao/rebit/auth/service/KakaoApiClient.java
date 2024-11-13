package kakao.rebit.auth.service;

import java.util.Objects;
import kakao.rebit.auth.dto.KakaoToken;
import kakao.rebit.auth.dto.KakaoUserInfo;
import kakao.rebit.auth.exception.UserInfoNotFoundException;
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

    @Value("${oauth.kakao.client-id}")
    private String clientId;

    @Value("${oauth.kakao.url.redirect-url}")
    private String redirectUri;

    @Value("${oauth.kakao.url.auth-url}")
    private String kakaoAuthUrl;

    @Value("${oauth.kakao.url.api-url}")
    private String kakaoApiUrl;

    @Value("${oauth.kakao.url.logout-url-template}")
    private String logoutUrlTemplate;

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
            throw UserInfoNotFoundException.EXCEPTION;
        }

        return userInfo;
    }

    public void logout() {
        String logoutUrl = String.format(logoutUrlTemplate, clientId, redirectUri);

        restClient.get()
            .uri(logoutUrl)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .toBodilessEntity();
    }
}
