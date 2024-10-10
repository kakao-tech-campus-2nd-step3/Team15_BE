package kakao.rebit.s3.exception;

import kakao.rebit.common.exception.BusinessException;

public class AwsDeleteServiceErrorException extends BusinessException {
    public static final BusinessException EXCEPTION = new AwsDeleteServiceErrorException();

    private AwsDeleteServiceErrorException(){
        super(S3ErrorCode.AWS_DELETE_SERVICE_ERROR);
    }
}
