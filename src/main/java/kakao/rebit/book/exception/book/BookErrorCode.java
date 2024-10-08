package kakao.rebit.book.exception.book;

import kakao.rebit.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum BookErrorCode implements ErrorCode {
    NOT_FOUND("BK001", HttpStatus.NOT_FOUND, "책을 찾을 수 없습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    BookErrorCode(String code, HttpStatus httpStatus, String message) {
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
