package kakao.rebit.feed.exception.feed;

import kakao.rebit.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum FeedErrorCode implements ErrorCode {
    NOT_FOUND("F001", HttpStatus.NOT_FOUND, "피드를 찾을 수 없습니다."),
    UPDATE_NOT_AUTHORIZED("F002", HttpStatus.FORBIDDEN, "피드 작성자만 수정할 수 있습니다."),
    DELETE_NOT_AUTHORIZED("F003", HttpStatus.FORBIDDEN, "피드 작성자만 삭제할 수 있습니다."),
    FAVORITE_BOOK_REQUIRED_BOOK("F004", HttpStatus.BAD_REQUEST, "인생책 피드는 책이 반드시 필요합니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    FeedErrorCode(String code, HttpStatus httpStatus, String message) {
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
