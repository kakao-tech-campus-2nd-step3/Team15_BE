package kakao.rebit.feed.e2e;

import static kakao.rebit.auth.util.AuthApiTestClient.login;
import static kakao.rebit.auth.util.FeedApiTestClient.createFavoriteBook;
import static kakao.rebit.auth.util.FeedApiTestClient.createLike;
import static kakao.rebit.auth.util.FeedApiTestClient.createMagazine;
import static kakao.rebit.auth.util.FeedApiTestClient.createStory;
import static kakao.rebit.auth.util.FeedApiTestClient.deleteFeed;
import static kakao.rebit.auth.util.FeedApiTestClient.deleteLike;
import static kakao.rebit.auth.util.FeedApiTestClient.getFeed;
import static kakao.rebit.auth.util.FeedApiTestClient.getFeeds;
import static kakao.rebit.auth.util.FeedApiTestClient.getLikesMember;
import static kakao.rebit.auth.util.MemberApiTestClient.deleteMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatList;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import java.util.List;
import kakao.rebit.auth.dto.LoginResponse;
import kakao.rebit.book.fixture.BookFixture;
import kakao.rebit.book.repository.BookRepository;
import kakao.rebit.common.PageTemplate;
import kakao.rebit.feed.dto.request.create.CreateFavoriteBookRequest;
import kakao.rebit.feed.dto.response.FeedResponse;
import kakao.rebit.feed.dto.response.LikesMemberResponse;
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
@DisplayName("피드 E2E 테스트")
class FeedE2ETest {

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

        createFavoriteBook(port, accessToken, FeedFixture.createFavoriteBookRequestWithBookId(bookId));
        createMagazine(port, accessToken, FeedFixture.createMagazineRequestWithBookId(bookId));
        createStory(port, accessToken, FeedFixture.createStoryRequestWithBookId(bookId));
    }

    @AfterEach
    void tearDown() {
        deleteMember(port, accessToken, memberId);
        bookRepository.deleteById(bookId);
    }

    @Test
    void 피드_목록_조회() {
        PageTemplate<FeedResponse> feedResponses = getFeeds(port)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(new TypeRef<>() {
                });

        List<FeedResponse> content = feedResponses.getContent();

        assertThatList(content).hasSize(3);
    }

    @Test
    void 피드_상세_조회() {
        // 피드 목록 조회
        PageTemplate<FeedResponse> feedResponses = getFeeds(port).extract().as(new TypeRef<>() {
        });
        List<FeedResponse> content = feedResponses.getContent();

        // 피드 상세 조회
        for (FeedResponse feedResponse : content) {
            FeedResponse response = getFeed(port, accessToken, feedResponse.getId())
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(FeedResponse.class);

            assertThat(response.getId()).isEqualTo(feedResponse.getId());
        }
    }

    @Test
    void 피드_삭제() {
        // 피드 목록 조회
        PageTemplate<FeedResponse> feedResponses = getFeeds(port).extract().as(new TypeRef<>() {
        });
        List<FeedResponse> content = feedResponses.getContent();

        // 피드 삭제
        for (FeedResponse feedResponse : content) {
            deleteFeed(port, accessToken, feedResponse.getId())
                    .statusCode(HttpStatus.NO_CONTENT.value());
        }

        // 피드 목록 조회
        PageTemplate<FeedResponse> emptyFeedResponses = getFeeds(port).extract().as(new TypeRef<>() {
        });
        List<FeedResponse> emptyContent = emptyFeedResponses.getContent();

        assertThat(emptyContent).isEmpty();
    }

    @Test
    void 좋아요_누른_멤버_조회() {
        // 좋아요 생성
        CreateFavoriteBookRequest createRequest = FeedFixture.createFavoriteBookRequestWithBookId(bookId);
        String feedLocation = createFavoriteBook(port, accessToken, createRequest).extract().header(HttpHeaders.LOCATION);
        String feedId = feedLocation.substring(feedLocation.lastIndexOf("/") + 1);

        // 좋아요 추가
        createLike(port, accessToken, feedId);

        // 좋아요 누른 멤버 조회 테스트
        PageTemplate<LikesMemberResponse> responses = getLikesMember(port, accessToken, feedId)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(new TypeRef<>() {
                });

        // 검증
        List<LikesMemberResponse> content = responses.getContent();
        for (LikesMemberResponse likesMemberResponse : content) {
            assertThat(likesMemberResponse.id()).isEqualTo(memberId);
        }
    }

    @Test
    void 좋아요_추가() {
        // 피드 생성
        CreateFavoriteBookRequest createRequest = FeedFixture.createFavoriteBookRequestWithBookId(bookId);
        String feedLocation = createFavoriteBook(port, accessToken, createRequest).extract().header(HttpHeaders.LOCATION);
        String feedId = feedLocation.substring(feedLocation.lastIndexOf("/") + 1);

        // 좋아요 추가
        createLike(port, accessToken, feedId).statusCode(HttpStatus.CREATED.value());

        // 피드 조회 후 좋아요 갯수가 1인지 확인
        FeedResponse feedResponse = getFeed(port, accessToken, Long.parseLong(feedId)).extract().as(FeedResponse.class);
        assertThat(feedResponse.getLikes()).isEqualTo(1);
    }

    @Test
    void 좋아요_삭제() {
        // 피드 생성
        CreateFavoriteBookRequest createRequest = FeedFixture.createFavoriteBookRequestWithBookId(bookId);
        String feedLocation = createFavoriteBook(port, accessToken, createRequest).extract().header(HttpHeaders.LOCATION);
        String feedId = feedLocation.substring(feedLocation.lastIndexOf("/") + 1);

        // 좋아요 생성
        createLike(port, accessToken, feedId);

        // 좋아요 삭제
        deleteLike(port, accessToken, feedId).statusCode(HttpStatus.NO_CONTENT.value());

        // 피드 조회 후 좋아요 갯수가 0인지 확인
        FeedResponse feedResponse = getFeed(port, accessToken, Long.parseLong(feedId)).extract().as(FeedResponse.class);
        assertThat(feedResponse.getLikes()).isEqualTo(0);
    }
}
