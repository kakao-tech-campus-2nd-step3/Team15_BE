package kakao.rebit.s3.exception;

import kakao.rebit.common.exception.BusinessException;

public class S3ErrorException extends BusinessException {

    public static final BusinessException EXCEPTION = new S3ErrorException();

    private S3ErrorException() {
        super(S3ErrorCode.S3_ERROR);
    }

}
