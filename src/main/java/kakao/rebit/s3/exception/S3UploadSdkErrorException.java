package kakao.rebit.s3.exception;

import kakao.rebit.common.exception.BusinessException;

public class S3UploadSdkErrorException extends BusinessException {

    public static final BusinessException EXCEPTION = new S3UploadSdkErrorException();

    private S3UploadSdkErrorException() {
        super(S3ErrorCode.S3_UPLOAD_SDK_ERROR);
    }
}
