package kakao.rebit.challenge.e2e;

import static kakao.rebit.auth.util.AuthApiTestClient.login;
import static kakao.rebit.auth.util.ChallengeApiTestClient.createChallenge;
import static kakao.rebit.auth.util.ChallengeApiTestClient.deleteChallenge;
import static kakao.rebit.auth.util.ChallengeApiTestClient.getChallenge;
import static kakao.rebit.auth.util.ChallengeApiTestClient.getChallenges;
import static kakao.rebit.auth.util.MemberApiTestClient.deleteMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatList;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import java.util.List;
import kakao.rebit.auth.dto.LoginResponse;
import kakao.rebit.challenge.dto.ChallengeRequest;
import kakao.rebit.challenge.dto.ChallengeResponse;
import kakao.rebit.challenge.entity.ChallengeType;
import kakao.rebit.challenge.exception.challenge.ChallengeErrorCode;
import kakao.rebit.challenge.fixture.ChallengeFixture;
import kakao.rebit.challenge.repository.ChallengeRepository;
import kakao.rebit.common.PageTemplate;
import kakao.rebit.common.exception.ErrorResponse;
import kakao.rebit.member.repository.MemberRepository;
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
@DisplayName("챌린지 E2E 테스트")
class ChallengeE2ETest {

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @LocalServerPort
    private int port;

    private String accessToken;
    private Long memberId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        LoginResponse login = login(port);

        accessToken = login.getToken().getAccessToken();
        memberId = login.getMemberId();

        List<ChallengeRequest> challengeRequests = List.of(
                ChallengeFixture.createRequest("챌린지1", "챌린지1 설명", ChallengeType.DAILY_WRITING),
                ChallengeFixture.createRequest("챌린지2", "챌린지2 설명", ChallengeType.RELAY_NOVEL),
                ChallengeFixture.createRequest("챌린지3", "챌린지3 설명", ChallengeType.SITUATIONAL_SENTENCE)
        );

        challengeRequests.forEach(request -> createChallenge(port, accessToken, request));
    }

    @AfterEach
    void tearDown() {
        deleteMember(port, accessToken, memberId);
    }

    @Test
    void 챌린지_생성() {
        ChallengeRequest request = ChallengeFixture.createDefaultRequest();

        String location = createChallenge(port, accessToken, request)
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

    @Test
    void 챌린지_목록_조회() {
        PageTemplate<ChallengeResponse> challengeResponses = getChallenges(port)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(new TypeRef<>() {
                });

        List<ChallengeResponse> content = challengeResponses.getContent();
        assertThatList(content).hasSize(3);
        assertThatList(content).extracting(ChallengeResponse::title)
                .containsExactly("챌린지3", "챌린지2", "챌린지1");
        assertThatList(content).extracting(ChallengeResponse::content)
                .containsExactly("챌린지3 설명", "챌린지2 설명", "챌린지1 설명");
        assertThatList(content).extracting(ChallengeResponse::type)
                .containsExactly(ChallengeType.SITUATIONAL_SENTENCE, ChallengeType.RELAY_NOVEL, ChallengeType.DAILY_WRITING);
    }

    @Test
    void 챌린지_상세_조회() {
        // 챌린지 목록 조회
        PageTemplate<ChallengeResponse> challengeResponses = getChallenges(port).extract().as(new TypeRef<>() {
        });
        List<ChallengeResponse> content = challengeResponses.getContent();

        // 챌린지 상세 조회
        for (ChallengeResponse challengeResponse : content) {
            ChallengeResponse response = getChallenge(port, accessToken, challengeResponse.id())
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ChallengeResponse.class);

            assertThat(response).isEqualTo(challengeResponse);
        }
    }

    @Test
    void 챌린지_삭제() {
        // 챌린지 목록 조회
        PageTemplate<ChallengeResponse> challengeResponses = getChallenges(port).extract().as(new TypeRef<>() {
        });
        List<ChallengeResponse> content = challengeResponses.getContent();

        // 챌린지 삭제
        for (ChallengeResponse challengeResponse : content) {
            deleteChallenge(port, accessToken, challengeResponse.id())
                    .statusCode(HttpStatus.NO_CONTENT.value());
        }

        // 챌린지 목록 조회
        PageTemplate<ChallengeResponse> emptyChallengeResponses = getChallenges(port).extract().as(new TypeRef<>() {
        });
        List<ChallengeResponse> emptyContent = emptyChallengeResponses.getContent();

        assertThat(emptyContent).isEmpty();
    }

    @Test
    void 챌린지_생성자가_아닌_사용자는_삭제할_수_없다() {
        // 다른 사용자로 로그인
        LoginResponse login = login(port);
        String otherAccessToken = login.getToken().getAccessToken();

        // 챌린지 목록 조회
        PageTemplate<ChallengeResponse> challengeResponses = getChallenges(port).extract().as(new TypeRef<>() {
        });
        List<ChallengeResponse> content = challengeResponses.getContent();

        // 챌린지 삭제
        for (ChallengeResponse challengeResponse : content) {
            ErrorResponse errorResponse = deleteChallenge(port, otherAccessToken, challengeResponse.id())
                    .statusCode(HttpStatus.FORBIDDEN.value())
                    .extract()
                    .as(ErrorResponse.class);

            assertThat(errorResponse.message()).isEqualTo(ChallengeErrorCode.DELETE_NOT_AUTHORIZED.getMessage());
        }
    }

    @Test
    void 진행_중이거나_종료된_챌린지는_삭제할_수_없다() {
        // 완료된 챌린지 생성
        memberRepository.findById(memberId).ifPresent(member ->
                challengeRepository.save(ChallengeFixture.createCompleted(member)));

        // 챌린지 목록 조회
        PageTemplate<ChallengeResponse> challengeResponses = getChallenges(port).extract().as(new TypeRef<>() {
        });
        List<ChallengeResponse> content = challengeResponses.getContent();
        ChallengeResponse challengeResponse = content.getFirst();

        // 챌린지 삭제
        ErrorResponse errorResponse = deleteChallenge(port, accessToken, challengeResponse.id())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(errorResponse.message()).isEqualTo(ChallengeErrorCode.DELETE_NOT_ALLOWED.getMessage());
    }
}
