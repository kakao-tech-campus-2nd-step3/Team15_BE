package kakao.rebit.s3.exception;

import kakao.rebit.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum S3ErrorCode implements ErrorCode {
    S3_ERROR("AWS001", HttpStatus.INTERNAL_SERVER_ERROR, "AWS API 오류 입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    S3ErrorCode(String code, HttpStatus httpStatus, String message) {
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
