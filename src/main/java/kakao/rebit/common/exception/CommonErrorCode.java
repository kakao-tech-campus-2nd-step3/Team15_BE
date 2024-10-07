package kakao.rebit.common.exception;

import org.springframework.http.HttpStatus;

public enum CommonErrorCode implements ErrorCode {
    INVALID_INPUT_VALUE("C001", "요청 값이 잘못되었습니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    CommonErrorCode(String code, String message) {
        this.code = code;
        this.httpStatus = HttpStatus.BAD_REQUEST;
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
