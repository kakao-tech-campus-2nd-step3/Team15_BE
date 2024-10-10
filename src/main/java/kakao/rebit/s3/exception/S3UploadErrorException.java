package kakao.rebit.s3.exception;

import kakao.rebit.common.exception.BusinessException;

public class S3UploadErrorException extends BusinessException {

    public static final BusinessException EXCEPTION = new S3UploadErrorException();

    private S3UploadErrorException() {
        super(S3ErrorCode.S3_UPLOAD_ERROR);
    }

}
