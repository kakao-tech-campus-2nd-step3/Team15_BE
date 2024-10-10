package kakao.rebit.s3.exception;

import kakao.rebit.common.exception.BusinessException;

public class S3DeleteUnknownErrorException extends BusinessException{
    public static final BusinessException EXCEPTION = new S3DeleteUnknownErrorException();

    private S3DeleteUnknownErrorException(){
        super(S3ErrorCode.S3_DELETE_UNKNOWN_ERROR);
    }
}
