package kakao.rebit.feed.exception.likes;

import kakao.rebit.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum LikesErrorCode implements ErrorCode {
    LIKES_ALREADY_PRESSED("L001", HttpStatus.BAD_REQUEST, "이미 좋아요를 누른 상태입니다."),
    LIKES_NOT_PRESSED("L002", HttpStatus.BAD_REQUEST, "좋아요를 누르지 않은 상태입니다."),
    FIND_NOT_AUTHORIZED("L003", HttpStatus.FORBIDDEN, "피드 작성자만 좋아요 목록을 확인할 수 있습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    LikesErrorCode(String code, HttpStatus httpStatus, String message) {
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
