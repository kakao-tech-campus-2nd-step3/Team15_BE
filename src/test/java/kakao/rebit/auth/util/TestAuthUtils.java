package kakao.rebit.auth.util;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import kakao.rebit.auth.dto.LoginResponse;

public class TestAuthUtils {

    private static final String LOGIN_URL = "/api/auth/login/oauth/kakao";

    public static String getTestToken() {
        LoginResponse response = given()
                .contentType(ContentType.JSON)
                .when()
                .get(LOGIN_URL + "?code=test-code")
                .then()
                .statusCode(200)
                .extract()
                .as(LoginResponse.class);

        return response.getToken().getAccessToken();
    }
}
