package kakao.rebit.s3.exception;

import kakao.rebit.common.exception.BusinessException;

public class S3UploadTimeOutErrorException extends BusinessException {

    public static final BusinessException EXCEPTION = new S3UploadTimeOutErrorException();

    private S3UploadTimeOutErrorException() {
        super(S3ErrorCode.S3_UPLOAD_TIMEOUT_ERROR);
    }
}
