package kakao.rebit.s3.exception;

import kakao.rebit.common.exception.BusinessException;

public class S3DeleteErrorException extends BusinessException {

    public static final BusinessException EXCEPTION = new S3DeleteErrorException();

    private S3DeleteErrorException() {
        super(S3ErrorCode.S3_DELETE_ERROR);
    }
}
