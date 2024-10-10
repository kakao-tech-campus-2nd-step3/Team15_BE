package kakao.rebit.s3.exception;

import kakao.rebit.common.exception.BusinessException;

public class S3UploadServiceErrorException extends BusinessException {

    public static final BusinessException EXCEPTION = new S3UploadServiceErrorException();

    private S3UploadServiceErrorException() {
        super(S3ErrorCode.S3_UPLOAD_SERVICE_ERROR);
    }

}
