package kakao.rebit.s3.exception;

import kakao.rebit.common.exception.BusinessException;

public class S3DeleteSdkErrorException extends BusinessException {

    public static final BusinessException EXCEPTION = new S3DeleteSdkErrorException();

    private S3DeleteSdkErrorException() {
        super(S3ErrorCode.S3_DELETE_SDK_ERROR);
    }
}
