package kakao.rebit.s3.exception;

import kakao.rebit.common.exception.BusinessException;

public class AwsUploadServiceErrorException extends BusinessException {

    public static final BusinessException EXCEPTION = new AwsUploadServiceErrorException();

    private AwsUploadServiceErrorException() {
        super(S3ErrorCode.AWS_UPLOAD_SERVICE_ERROR);
    }
}
