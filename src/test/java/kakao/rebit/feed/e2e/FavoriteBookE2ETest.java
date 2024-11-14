package kakao.rebit.feed.e2e;

import static kakao.rebit.auth.util.AuthApiTestClient.login;
import static kakao.rebit.auth.util.FeedApiTestClient.createFavoriteBook;
import static kakao.rebit.auth.util.FeedApiTestClient.getFeed;
import static kakao.rebit.auth.util.FeedApiTestClient.updateFavoriteBook;
import static kakao.rebit.auth.util.MemberApiTestClient.deleteMember;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import kakao.rebit.auth.dto.LoginResponse;
import kakao.rebit.book.fixture.BookFixture;
import kakao.rebit.book.repository.BookRepository;
import kakao.rebit.feed.dto.request.create.CreateFavoriteBookRequest;
import kakao.rebit.feed.dto.request.update.UpdateFavoriteBookRequest;
import kakao.rebit.feed.dto.response.FavoriteBookResponse;
import kakao.rebit.feed.fixture.FeedFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("인생책 E2E 테스트")
class FavoriteBookE2ETest {

    @LocalServerPort
    private int port;

    private String accessToken;
    private Long memberId;
    private Long bookId;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        LoginResponse login = login(port);

        accessToken = login.getToken().getAccessToken();
        memberId = login.getMemberId();

        bookId = bookRepository.save(BookFixture.createDefault()).getId();
    }

    @AfterEach
    void tearDown() {
        deleteMember(port, accessToken, memberId);
        bookRepository.deleteById(bookId);
    }

    @Test
    void 인생책_생성() {
        CreateFavoriteBookRequest request = FeedFixture.createFavoriteBookRequestWithBookId(bookId);

        String location = createFavoriteBook(port, accessToken, request)
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .header(HttpHeaders.LOCATION);

        assertThat(location).matches("/feeds/\\d+");

        String id = location.substring(location.lastIndexOf("/") + 1);
        assertThat(id).matches("\\d+");
        assertThat(Long.parseLong(id)).isPositive();
    }

    @Test
    void 인생책_수정() {
        // 인생책 생성
        CreateFavoriteBookRequest createRequest = FeedFixture.createFavoriteBookRequestWithBookId(bookId);
        String location = createFavoriteBook(port, accessToken, createRequest).extract().header(HttpHeaders.LOCATION);
        String favoriteBookId = location.substring(location.lastIndexOf("/") + 1);

        // 업데이트 테스트 실행 후 상태코드 검증
        UpdateFavoriteBookRequest request = FeedFixture.updateFavoriteBookRequestWithBookId(bookId);
        updateFavoriteBook(port, accessToken, favoriteBookId, request).statusCode(HttpStatus.NO_CONTENT.value());

        // 검증
        FavoriteBookResponse favoriteBookResponse = getFeed(port, accessToken, Long.parseLong(favoriteBookId)).extract()
                .as(FavoriteBookResponse.class);

        assertThat(favoriteBookResponse.getBriefReview()).isEqualTo(request.briefReview());
        assertThat(favoriteBookResponse.getFullReview()).isEqualTo(request.fullReview());
    }
}
