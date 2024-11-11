package kakao.rebit.wishlist.exception;

import kakao.rebit.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum WishlistErrorCode implements ErrorCode {
    WISHLIST_BOOK_NOT_FOUND("WL001", HttpStatus.NOT_FOUND, "위시리스트에서 해당 책을 찾을 수 없습니다."),
    WISHLIST_CHALLENGE_NOT_FOUND("WL002", HttpStatus.NOT_FOUND, "위시리스트에서 해당 챌린지를 찾을 수 없습니다."),
    ALREADY_IN_WISHLIST("WL003", HttpStatus.CONFLICT, "이미 위시리스트에 존재합니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    WishlistErrorCode(String code, HttpStatus httpStatus, String message) {
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
