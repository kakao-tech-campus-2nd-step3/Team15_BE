package kakao.rebit.s3.exception;

import kakao.rebit.common.exception.BusinessException;

public class AwsDownloadServiceErrorException extends BusinessException {

    public static final BusinessException EXCEPTION = new AwsDownloadServiceErrorException();

    private AwsDownloadServiceErrorException() {
        super(S3ErrorCode.AW3_DOWNLOAD_SERVICE_ERROR);
    }
}
