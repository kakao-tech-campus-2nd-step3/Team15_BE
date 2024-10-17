package kakao.rebit.common.exception;

import org.springframework.http.HttpStatus;

public enum ImageErrorCode implements ErrorCode {
    IMAGE_DOWNLOAD_ERROR("IM001", HttpStatus.INTERNAL_SERVER_ERROR, "이미지 다운로드에 실패하였습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    ImageErrorCode(String code, HttpStatus httpStatus, String message) {
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
