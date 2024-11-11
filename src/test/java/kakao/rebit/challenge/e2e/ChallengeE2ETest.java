package kakao.rebit.challenge.e2e;

import static io.restassured.RestAssured.given;
import static kakao.rebit.auth.util.TestAuthUtils.getTestToken;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import kakao.rebit.challenge.dto.ChallengeRequest;
import kakao.rebit.challenge.fixture.ChallengeFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("챌린지 E2E 테스트")
class ChallengeE2ETest {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String CHALLENGE_URL = "/api/challenges";

    @LocalServerPort
    private int port;

    private String accessToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        accessToken = getTestToken();
    }

    @Test
    void 챌린지_생성() {
        ChallengeRequest request = ChallengeFixture.createRequest();

        String location = given()
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(CHALLENGE_URL)
                .then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .header(HttpHeaders.LOCATION);

        // 경로 패턴 검증
        assertThat(location).matches("/challenges/\\d+");

        // ID 추출 후 숫자 검증
        String id = location.substring(location.lastIndexOf("/") + 1);
        assertThat(id).matches("\\d+");         // 숫자인지 검증
        assertThat(Long.parseLong(id)).isPositive();  // 양수인지 검증
    }
}
