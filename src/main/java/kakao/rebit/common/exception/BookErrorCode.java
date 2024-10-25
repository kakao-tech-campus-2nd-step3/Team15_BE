package kakao.rebit.common.exception;

import org.springframework.http.HttpStatus;

public enum BookErrorCode implements ErrorCode {
    BOOK_NOT_FOUND("B001", HttpStatus.NOT_FOUND, "ISBN %s 에 해당하는 책을 찾을 수 없습니다.");

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
