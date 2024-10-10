package kakao.rebit.s3.exception;

import kakao.rebit.common.exception.BusinessException;

public class S3DeleteServiceErrorException extends BusinessException{
    public static final BusinessException EXCEPTION = new S3DeleteServiceErrorException();

    private S3DeleteServiceErrorException(){
        super(S3ErrorCode.S3_DELETE_SERVICE_ERROR);
    }
}
