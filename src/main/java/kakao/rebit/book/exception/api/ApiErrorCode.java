package kakao.rebit.book.exception.api;

import kakao.rebit.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum ApiErrorCode implements ErrorCode {
    API_ERROR("AP001", HttpStatus.INTERNAL_SERVER_ERROR, "API 호출 중 오류가 발생했습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    ApiErrorCode(String code, HttpStatus httpStatus, String message) {
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
