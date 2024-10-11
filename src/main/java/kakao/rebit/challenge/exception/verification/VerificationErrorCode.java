package kakao.rebit.challenge.exception.verification;

import kakao.rebit.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum VerificationErrorCode implements ErrorCode {
    VERIFICATION_NOT_FOUND("CV001", HttpStatus.NOT_FOUND, "인증글을 찾을 수 없습니다."),
    DELETE_NOT_AUTHORIZED("CV002", HttpStatus.FORBIDDEN, "본인이 올린 인증글만 삭제할 수 있습니다"),
    VERIFY_CHALLENGE_NOT_ON_GOING("CV003", HttpStatus.BAD_REQUEST, "챌린지 진행 중에만 인증글을 작성할 수 있습니다."),
    VERIFY_CHALLENGE_ALREADY_DONE_TODAY("CV004", HttpStatus.BAD_REQUEST, "오늘 이미 인증글을 작성했습니다.")
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    VerificationErrorCode(String code, HttpStatus httpStatus, String message) {
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
