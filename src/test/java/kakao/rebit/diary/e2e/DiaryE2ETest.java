package kakao.rebit.diary.e2e;

import static kakao.rebit.auth.util.AuthApiTestClient.login;
import static kakao.rebit.auth.util.DiaryApiTestClient.createDiary;
import static kakao.rebit.auth.util.DiaryApiTestClient.deleteDiary;
import static kakao.rebit.auth.util.DiaryApiTestClient.getDiary;
import static kakao.rebit.auth.util.DiaryApiTestClient.getDiaries;
import static kakao.rebit.auth.util.DiaryApiTestClient.updateDiary;
import static kakao.rebit.auth.util.MemberApiTestClient.deleteMember;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import java.util.List;
import kakao.rebit.auth.dto.LoginResponse;
import kakao.rebit.book.entity.Book;
import kakao.rebit.book.fixture.BookFixture;
import kakao.rebit.book.repository.BookRepository;
import kakao.rebit.common.PageTemplate;
import kakao.rebit.diary.dto.DiaryRequest;
import kakao.rebit.diary.dto.DiaryResponse;
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
@DisplayName("독서일기 E2E 테스트")
class DiaryE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private BookRepository bookRepository;

    private String accessToken;
    private Long memberId;
    private Book defaultBook;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        LoginResponse login = login(port);

        accessToken = login.getToken().getAccessToken();
        memberId = login.getMemberId();

        defaultBook = bookRepository.save(BookFixture.createDefault());

        List<DiaryRequest> diaryRequests = List.of(
                new DiaryRequest("일기1", defaultBook.getIsbn(), "2024-11-01"),
                new DiaryRequest("일기2", defaultBook.getIsbn(), "2024-11-02"),
                new DiaryRequest("일기3", defaultBook.getIsbn(), "2024-11-03")
        );

        diaryRequests.forEach(request -> createDiary(port, accessToken, request));
    }

    @AfterEach
    void tearDown() {
        deleteMember(port, accessToken, memberId);
        bookRepository.deleteById(defaultBook.getId());
    }

    @Test
    void 독서일기_생성() {
        DiaryRequest request = new DiaryRequest("새로운 일기", defaultBook.getIsbn(), "2024-11-04");

        String location = createDiary(port, accessToken, request)
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .header(HttpHeaders.LOCATION);

        assertThat(location).matches("/api/diaries/\\d+");

        String id = location.substring(location.lastIndexOf("/") + 1);
        assertThat(id).matches("\\d+");
        assertThat(Long.parseLong(id)).isPositive();
    }

    @Test
    void 독서일기_목록_조회() {
        PageTemplate<DiaryResponse> diaryResponses = getDiaries(port, accessToken)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(new TypeRef<>() {
                });

        List<DiaryResponse> content = diaryResponses.getContent();
        assertThat(content).hasSize(3);
        assertThat(content).extracting(DiaryResponse::content)
                .containsExactly("일기1", "일기2", "일기3");
        assertThat(content).extracting(DiaryResponse::date)
                .containsExactly("2024-11-01", "2024-11-02", "2024-11-03");
    }

    @Test
    void 독서일기_상세_조회() {
        PageTemplate<DiaryResponse> diaryResponses = getDiaries(port, accessToken).extract().as(new TypeRef<>() {
        });
        List<DiaryResponse> content = diaryResponses.getContent();

        for (DiaryResponse diaryResponse : content) {
            DiaryResponse response = getDiary(port, accessToken, diaryResponse.id())
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(DiaryResponse.class);

            assertThat(response).isEqualTo(diaryResponse);
        }
    }

    @Test
    void 독서일기_수정() {
        PageTemplate<DiaryResponse> diaryResponses = getDiaries(port, accessToken)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(new TypeRef<>() {
                });
        List<DiaryResponse> content = diaryResponses.getContent();
        DiaryResponse diaryToEdit = content.get(0);

        DiaryRequest updatedRequest = new DiaryRequest("수정된 일기 내용", diaryToEdit.book().isbn(), diaryToEdit.date());

        // 다이어리 수정 요청
        updateDiary(port, accessToken, diaryToEdit.id(), updatedRequest)
                .statusCode(HttpStatus.NO_CONTENT.value());

        // 수정된 내용 확인
        DiaryResponse updatedDiary = getDiary(port, accessToken, diaryToEdit.id())
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(DiaryResponse.class);

        assertThat(updatedDiary.content()).isEqualTo("수정된 일기 내용");
        assertThat(updatedDiary.date()).isEqualTo(diaryToEdit.date());
    }

    @Test
    void 독서일기_삭제() {
        PageTemplate<DiaryResponse> diaryResponses = getDiaries(port, accessToken).extract().as(new TypeRef<>() {
        });
        List<DiaryResponse> content = diaryResponses.getContent();

        for (DiaryResponse diaryResponse : content) {
            deleteDiary(port, accessToken, diaryResponse.id())
                    .statusCode(HttpStatus.NO_CONTENT.value());
        }

        PageTemplate<DiaryResponse> emptyDiaryResponses = getDiaries(port, accessToken).extract().as(new TypeRef<>() {
        });
        List<DiaryResponse> emptyContent = emptyDiaryResponses.getContent();

        assertThat(emptyContent).isEmpty();
    }
}
