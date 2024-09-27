package kakao.rebit.auth.service;

import java.util.HashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kakao.rebit.auth.dto.KakaoUserInfo;

@Service
public class KakaoApiClient {

    private static final String CONTENT_TYPE = "Content-type";
    private static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded;charset=utf-8";
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Value("${OAUTH_KAKAO_CLIENT_ID}")
    private String clientId;

    @Value("${OAUTH_KAKAO_REDIRECT_URI}")
    private String redirectUri;

    @Value("${OAUTH_KAKAO_AUTH_URL}")
    private String kakaoAuthUrl;

    @Value("${OAUTH_KAKAO_API_URL}")
    private String kakaoApiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public KakaoApiClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public String getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYPE, APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = createTokenParams(code);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        String tokenUrl = kakaoAuthUrl + "/oauth/token";

        ResponseEntity<JsonNode> response = restTemplate.exchange(
            tokenUrl,
            HttpMethod.POST,
            request,
            JsonNode.class
        );

        JsonNode rootNode = response.getBody();
        if (rootNode != null) {
            String accessToken = rootNode.path("access_token").asText();
            if (accessToken == null || accessToken.isEmpty()) {
                throw new RuntimeException("액세스 토큰을 가져오지 못했습니다.");
            }
            return accessToken;
        } else {
            throw new RuntimeException("응답이 비어 있습니다.");
        }
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
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, BEARER_PREFIX + accessToken);
        headers.add(CONTENT_TYPE, APPLICATION_FORM_URLENCODED);

        HttpEntity<?> request = new HttpEntity<>(headers);
        String userInfoUrl = kakaoApiUrl + "/v2/user/me";

        ResponseEntity<KakaoUserInfo> response = restTemplate.exchange(
            userInfoUrl,
            HttpMethod.POST,
            request,
            KakaoUserInfo.class
        );

        KakaoUserInfo userInfo = response.getBody();
        if (userInfo == null) {
            throw new RuntimeException("사용자 정보가 비어 있습니다.");
        }

        return userInfo;
    }
}
