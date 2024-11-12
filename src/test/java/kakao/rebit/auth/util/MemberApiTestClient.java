package kakao.rebit.auth.util;

import static io.restassured.RestAssured.given;

import io.restassured.response.ValidatableResponse;
import org.springframework.http.HttpHeaders;

public class MemberApiTestClient {
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String MEMBER_URL = "/api/members";

    public static ValidatableResponse deleteMember(int port, String accessToken, Long memberId) {
        return given()
                .port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
                .when()
                .delete(MEMBER_URL + "/" + memberId)
                .then()
                .log().all();
    }
}
