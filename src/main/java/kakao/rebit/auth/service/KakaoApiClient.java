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

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        String tokenUrl = kakaoAuthUrl + "/oauth/token";

        ResponseEntity<String> response = restTemplate.exchange(
            tokenUrl,
            HttpMethod.POST,
            request,
            String.class
        );

        try {
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            String accessToken = rootNode.path("access_token").asText();

            if (accessToken == null || accessToken.isEmpty()) {
                throw new RuntimeException("액세스 토큰을 가져오지 못했습니다.");
            }

            return accessToken;
        } catch (Exception e) {
            throw new RuntimeException("액세스 토큰을 파싱하는 데 실패했습니다.", e);
        }
    }

    public HashMap<String, Object> getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, BEARER_PREFIX + accessToken);
        headers.add(CONTENT_TYPE, APPLICATION_FORM_URLENCODED);

        HttpEntity<?> request = new HttpEntity<>(headers);
        String userInfoUrl = kakaoApiUrl + "/v2/user/me";

        ResponseEntity<String> response = restTemplate.exchange(
            userInfoUrl,
            HttpMethod.POST,
            request,
            String.class
        );

        System.out.println("카카오 API 응답: " + response.getBody());

        HashMap<String, Object> userInfo = new HashMap<>();
        try {
            JsonNode jsonNode = objectMapper.readTree(response.getBody());

            JsonNode idNode = jsonNode.get("id");
            if (idNode != null) {
                userInfo.put("id", idNode.asLong());
            } else {
                throw new RuntimeException("응답에 ID 필드가 없습니다.");
            }

            JsonNode kakaoAccountNode = jsonNode.path("kakao_account");
            if (kakaoAccountNode != null && kakaoAccountNode.get("email") != null) {
                userInfo.put("email", kakaoAccountNode.get("email").asText());
            } else {
                userInfo.put("email", null);
            }

            JsonNode propertiesNode = jsonNode.path("properties");
            if (propertiesNode != null && propertiesNode.get("nickname") != null) {
                userInfo.put("nickname", propertiesNode.get("nickname").asText());
            } else {
                userInfo.put("nickname", null);
            }

        } catch (Exception e) {
            throw new RuntimeException("사용자 정보를 파싱하는 데 실패했습니다.", e);
        }
        return userInfo;
    }
}
