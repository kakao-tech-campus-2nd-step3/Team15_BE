package kakao.rebit.s3.exception;

import kakao.rebit.common.exception.BusinessException;

public class S3UploadClientErrorException extends BusinessException {

    public static final BusinessException EXCEPTION = new S3UploadClientErrorException();

    private S3UploadClientErrorException() {
        super(S3ErrorCode.S3_UPLOAD_CLIENT_ERROR);
    }
}
