package kakao.rebit.challenge.exception.challenge;

import kakao.rebit.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum ChallengeErrorCode implements ErrorCode {
    NOT_FOUND("CH001", HttpStatus.NOT_FOUND, "챌린지를 찾을 수 없습니다."),
    DELETE_NOT_AUTHORIZED("CH002", HttpStatus.FORBIDDEN, "챌린지 주최자만 삭제할 수 있습니다."),
    DELETE_NOT_ALLOWED("CH003", HttpStatus.BAD_REQUEST, "진행 중이거나 종료된 챌린지는 삭제할 수 없습니다."),
    NOT_RECRUITING("CH004", HttpStatus.BAD_REQUEST, "모집 기간이 아닙니다."),
    FULL("CH005", HttpStatus.BAD_REQUEST, "모집 인원이 가득 찼습니다."),
    ENTRY_FEE_NOT_ENOUGH("CH006", HttpStatus.BAD_REQUEST, "최소 예치금보다 적은 금액으로 챌린지를 참여할 수 없습니다."),

    // 생성 검증
    INVALID_RECRUITMENT_PERIOD("CH007", HttpStatus.BAD_REQUEST, "모집 종료일은 모집 시작일보다 이후여야 합니다."),
    INVALID_CHALLENGE_PERIOD("CH008", HttpStatus.BAD_REQUEST, "챌린지 종료일은 챌린지 시작일보다 이후여야 합니다."),
    INVALID_CHALLENGE_START_DATE("CH009", HttpStatus.BAD_REQUEST, "챌린지 시작일은 모집 종료일보다 이후여야 합니다."),
    INVALID_HEADCOUNT_RANGE("CH010", HttpStatus.BAD_REQUEST, "최대 참가자 수는 최소 참가자 수보다 커야 합니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    ChallengeErrorCode(String code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
