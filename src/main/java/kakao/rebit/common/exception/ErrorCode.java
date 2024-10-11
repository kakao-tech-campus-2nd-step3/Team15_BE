package kakao.rebit.common.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    String getCode();
    HttpStatus getHttpStatus();
    String getMessage();

    default String getHttpStatusCodeAndName() {
        return getHttpStatus().value() + " " + getHttpStatus().name();
    }
}
