package kakao.rebit.s3.exception;

import kakao.rebit.common.exception.BusinessException;

public class S3DeleteClientErrorException extends BusinessException {
    public static final BusinessException EXCEPTION = new S3DeleteClientErrorException();

    private S3DeleteClientErrorException(){
        super(S3ErrorCode.S3_DELETE_CLIENT_ERROR);
    }

}
