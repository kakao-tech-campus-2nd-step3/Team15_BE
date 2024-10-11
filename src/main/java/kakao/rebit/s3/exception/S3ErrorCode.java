package kakao.rebit.s3.exception;

import kakao.rebit.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum S3ErrorCode implements ErrorCode {
    S3_UPLOAD_ERROR("AWS001", HttpStatus.INTERNAL_SERVER_ERROR, "[UPLOAD] S3 서비스 오류입니다."),
    S3_UPLOAD_CLIENT_ERROR("AWS002", HttpStatus.BAD_REQUEST, "[UPLOAD] 클라이언트 오류입니다."),
    S3_UPLOAD_SDK_ERROR("AWS003", HttpStatus.GATEWAY_TIMEOUT, "[UPLOAD] AWS SDK 오류입니다."),
    S3_UPLOAD_UNKNOWN_ERROR("AWS004", HttpStatus.INTERNAL_SERVER_ERROR, "[UPLOAD] 예상치 못한 오류가 발생했습니다."),
    S3_DOWNLOAD_ERROR("AWS005", HttpStatus.INTERNAL_SERVER_ERROR, "[DOWNLOAD] S3 서비스 오류입니다."),
    S3_DOWNLOAD_CLIENT_ERROR("AWS006", HttpStatus.BAD_REQUEST, "[DOWNLOAD] 클라이언트 오류입니다."),
    S3_DOWNLOAD_SDK_ERROR("AWS007", HttpStatus.GATEWAY_TIMEOUT, "[DOWNLOAD] AWS SDK 오류입니다."),
    S3_DOWNLOAD_UNKNOWN_ERROR("AWS008", HttpStatus.INTERNAL_SERVER_ERROR, "[DOWNLOAD] 예상치 못한 오류가 발생했습니다."),
    S3_DELETE_ERROR("AWS009", HttpStatus.INTERNAL_SERVER_ERROR, "[DELETE] S3 서비스 오류입니다."),
    S3_DELETE_CLIENT_ERROR("AWS010", HttpStatus.BAD_REQUEST, "[DELETE] 클라이언트 오류입니다."),
    S3_DELETE_SDK_ERROR("AWS011", HttpStatus.GATEWAY_TIMEOUT, "[DELETE] AWS SDK 오류입니다."),
    S3_DELETE_UNKNOWN_ERROR("AWS012", HttpStatus.INTERNAL_SERVER_ERROR, "[DELETE] 예상치 못한 오류가 발생했습니다."),
    S3_NOT_ALLOWED_FILE_FORMAT("AWS013", HttpStatus.BAD_REQUEST, "[UPLOAD] 지원하지 않는 파일 형식입니다.");

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
