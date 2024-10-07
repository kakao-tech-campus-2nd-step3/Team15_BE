package kakao.rebit.challenge.exception.participation;

import kakao.rebit.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum ParticipationErrorCode implements ErrorCode {
    NOT_FOUND("CP001", HttpStatus.NOT_FOUND, "챌린지 참여 정보를 찾을 수 없습니다."),
    ALREADY_EXISTS("CP002", HttpStatus.BAD_REQUEST, "이미 참여한 챌린지입니다."),
    NOT_PARTICIPANT("CP003", HttpStatus.BAD_REQUEST, "참여하지 않은 챌린지입니다.")
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    ParticipationErrorCode(String code, HttpStatus httpStatus, String message) {
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
