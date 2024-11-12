package kakao.rebit.auth.util;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import kakao.rebit.challenge.dto.ChallengeRequest;
import org.springframework.http.HttpHeaders;

public class ChallengeApiTestClient {
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String CHALLENGE_URL = "/api/challenges";

    public static ValidatableResponse createChallenge(int port, String accessToken, ChallengeRequest request) {
        return given()
                .port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(CHALLENGE_URL)
                .then()
                .log().all();
    }

    public static ValidatableResponse getChallenges(int port) {
        return given()
                .port(port)
                .when()
                .get(CHALLENGE_URL)
                .then()
                .log().all();
    }

    public static ValidatableResponse getChallenge(int port, String accessToken, Long challengeId) {
        return given()
                .port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
                .when()
                .get(CHALLENGE_URL + "/" + challengeId)
                .then()
                .log().all();
    }

    public static ValidatableResponse deleteChallenge(int port, String accessToken, Long challengeId) {
        return given()
                .port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
                .when()
                .delete(CHALLENGE_URL + "/" + challengeId)
                .then()
                .log().all();
    }
}
