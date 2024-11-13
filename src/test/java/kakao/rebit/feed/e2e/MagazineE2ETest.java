package kakao.rebit.feed.e2e;

import static kakao.rebit.auth.util.AuthApiTestClient.login;
import static kakao.rebit.auth.util.FeedApiTestClient.createMagazine;
import static kakao.rebit.auth.util.FeedApiTestClient.getFeed;
import static kakao.rebit.auth.util.FeedApiTestClient.updateMagazine;
import static kakao.rebit.auth.util.MemberApiTestClient.deleteMember;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import kakao.rebit.auth.dto.LoginResponse;
import kakao.rebit.book.fixture.BookFixture;
import kakao.rebit.book.repository.BookRepository;
import kakao.rebit.feed.dto.request.create.CreateMagazineRequest;
import kakao.rebit.feed.dto.request.update.UpdateMagazineRequest;
import kakao.rebit.feed.dto.response.MagazineResponse;
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
@DisplayName("메거진 E2E 테스트")
public class MagazineE2ETest {

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
    void 메거진_생성() {
        CreateMagazineRequest request = FeedFixture.createMagazineRequestWithBookId(bookId);

        String location = createMagazine(port, accessToken, request)
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .header(HttpHeaders.LOCATION);

        // 경로 패턴 검증
        assertThat(location).matches("/feeds/\\d+");

        // ID 추출 후 숫자 검증
        String id = location.substring(location.lastIndexOf("/") + 1);
        assertThat(id).matches("\\d+");         // 숫자인지 검증
        assertThat(Long.parseLong(id)).isPositive();  // 양수인지 검증
    }

    @Test
    void 메거진_수정() {
        // 메거진 생성
        CreateMagazineRequest createRequest = FeedFixture.createMagazineRequestWithBookId(bookId);
        String location = createMagazine(port, accessToken, createRequest).extract().header(HttpHeaders.LOCATION);
        String magazineId = location.substring(location.lastIndexOf("/") + 1);

        // 업데이트 테스트 실행 후 상태코드 검증
        UpdateMagazineRequest request = FeedFixture.updateMagazineRequestWithBookId(bookId);
        updateMagazine(port, accessToken, magazineId, request).statusCode(HttpStatus.NO_CONTENT.value());

        // 검증
        MagazineResponse magazineResponse = getFeed(port, accessToken, Long.parseLong(magazineId)).extract().as(MagazineResponse.class);
        assertThat(magazineResponse.getContent()).isEqualTo(request.content());
    }
}
