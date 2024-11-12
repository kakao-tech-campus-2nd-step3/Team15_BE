package kakao.rebit.auth.util;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import kakao.rebit.auth.dto.LoginResponse;
import org.springframework.http.HttpStatus;

public class AuthApiTestClient {

    private static final String AUTH_URL = "/api/auth";
    private static final String LOGIN_URL = "/login/oauth/kakao";

    public static LoginResponse login(int port) {
        return given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get(AUTH_URL + LOGIN_URL + "?code=test-code")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(LoginResponse.class);
    }
}
