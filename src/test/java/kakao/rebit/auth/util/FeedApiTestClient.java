package kakao.rebit.auth.util;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import kakao.rebit.feed.dto.request.create.CreateFavoriteBookRequest;
import kakao.rebit.feed.dto.request.create.CreateMagazineRequest;
import kakao.rebit.feed.dto.request.create.CreateStoryRequest;
import kakao.rebit.feed.dto.request.update.UpdateFavoriteBookRequest;
import kakao.rebit.feed.dto.request.update.UpdateMagazineRequest;
import kakao.rebit.feed.dto.request.update.UpdateStoryRequest;
import org.springframework.http.HttpHeaders;

public class FeedApiTestClient {
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String FEED_URL = "/api/feeds";
    private static final String FAVORITE_BOOK_URL = "/api/feeds/favorite-books";
    private static final String MAGAZINE_URL = "/api/feeds/magazines";
    private static final String STORY_URL = "/api/feeds/stories";

    public static ValidatableResponse getFeeds(int port) {
        return given()
                .port(port)
                .when()
                .get(FEED_URL)
                .then()
                .log().all();
    }

    public static ValidatableResponse getFeed(int port, String accessToken, Long feedId) {
        return given()
                .port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
                .when()
                .get(FEED_URL + "/" + feedId)
                .then()
                .log().all();
    }

    public static ValidatableResponse createFavoriteBook(int port, String accessToken, CreateFavoriteBookRequest request) {
        return given()
                .port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(FEED_URL)
                .then()
                .log().all();
    }

    public static ValidatableResponse createMagazine(int port, String accessToken, CreateMagazineRequest request) {
        return given()
                .port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(FEED_URL)
                .then()
                .log().all();
    }

    public static ValidatableResponse createStory(int port, String accessToken, CreateStoryRequest request) {
        return given()
                .port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(FEED_URL)
                .then()
                .log().all();
    }

    public static ValidatableResponse updateFavoriteBook(int port, String accessToken, String favoriteBookId, UpdateFavoriteBookRequest request) {
        return given()
                .port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put(FAVORITE_BOOK_URL + "/" + favoriteBookId)
                .then()
                .log().all();
    }

    public static ValidatableResponse updateMagazine(int port, String accessToken, String magazineId, UpdateMagazineRequest request) {
        return given()
                .port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put(MAGAZINE_URL + "/" + magazineId)
                .then()
                .log().all();
    }

    public static ValidatableResponse updateStory(int port, String accessToken, String storyId, UpdateStoryRequest request) {
        return given()
                .port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put(STORY_URL + "/" + storyId)
                .then()
                .log().all();
    }

    public static ValidatableResponse deleteFeed(int port, String accessToken, Long feedId) {
        return given()
                .port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
                .when()
                .delete(FEED_URL + "/" + feedId)
                .then()
                .log().all();
    }

    public static ValidatableResponse getLikesMember(int port, String accessToken, String feedId) {
        return given()
                .port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
                .when()
                .get(FEED_URL + "/" + feedId + "/likes")
                .then()
                .log().all();
    }

    public static ValidatableResponse createLike(int port, String accessToken, String feedId) {
        return given()
                .port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
                .contentType(ContentType.JSON)
                .when()
                .post(FEED_URL + "/" + feedId + "/likes")
                .then()
                .log().all();
    }

    public static ValidatableResponse deleteLike(int port, String accessToken, String feedId) {
        return given()
                .port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
                .when()
                .delete(FEED_URL + "/" + feedId + "/likes")
                .then()
                .log().all();
    }
}
