package kakao.rebit.auth.jwt.exception;

import kakao.rebit.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum JwtErrorCode implements ErrorCode {

    INVALID_TOKEN("JT001", HttpStatus.UNAUTHORIZED, "유효하지 않는 토큰입니다."),
    EXPIRED_TOKEN("JT002", HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    SIGNATURE_VALIDATION_FAILED("JT003", HttpStatus.UNAUTHORIZED, "유효하지 않는 서명입니다."),
    MISSING_TOKEN("JT004", HttpStatus.UNAUTHORIZED, "요청에 토큰이 포함되어있지 않습니다."),
    UNSUPPORTED_TOKEN("JT005", HttpStatus.BAD_REQUEST, "지원되지 않는 토큰입니다."),
    ACCESS_DENIED("JT006", HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");
    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    JwtErrorCode(String code, HttpStatus httpStatus, String message) {
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
