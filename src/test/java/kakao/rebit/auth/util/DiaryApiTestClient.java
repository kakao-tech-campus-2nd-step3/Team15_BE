package kakao.rebit.auth.util;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import kakao.rebit.diary.dto.DiaryRequest;
import org.springframework.http.HttpHeaders;

public class DiaryApiTestClient {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String DIARY_URL = "/api/diaries";

    public static ValidatableResponse createDiary(int port, String accessToken, DiaryRequest diaryRequest) {
        return given()
                .port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
                .contentType(ContentType.JSON)
                .body(diaryRequest)
                .when()
                .post(DIARY_URL)
                .then()
                .log().all();
    }

    public static ValidatableResponse getDiaries(int port, String accessToken) {
        return given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .port(port)
                .when()
                .get(DIARY_URL)
                .then()
                .log().all();
    }

    public static ValidatableResponse getDiary(int port, String accessToken, Long diaryId) {
        return given()
                .port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
                .when()
                .get(DIARY_URL + "/" + diaryId)
                .then()
                .log().all();
    }

    public static ValidatableResponse updateDiary(int port, String accessToken, Long diaryId, DiaryRequest diaryRequest) {
        return given()
                .port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
                .contentType(ContentType.JSON)
                .body(diaryRequest)
                .when()
                .put(DIARY_URL + "/" + diaryId)
                .then()
                .log().all();
    }

    public static ValidatableResponse deleteDiary(int port, String accessToken, Long diaryId) {
        return given()
                .port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
                .when()
                .delete(DIARY_URL + "/" + diaryId)
                .then()
                .log().all();
    }
}
