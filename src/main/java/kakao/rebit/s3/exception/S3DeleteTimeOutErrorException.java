package kakao.rebit.s3.exception;

import kakao.rebit.common.exception.BusinessException;

public class S3DeleteTimeOutErrorException extends BusinessException {

    public static final BusinessException EXCEPTION = new S3DeleteTimeOutErrorException();

    private S3DeleteTimeOutErrorException() {
        super(S3ErrorCode.S3_DELETE_TIMEOUT_ERROR);
    }
}
