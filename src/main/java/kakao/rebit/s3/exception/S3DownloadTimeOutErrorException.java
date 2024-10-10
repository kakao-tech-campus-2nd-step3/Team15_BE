package kakao.rebit.s3.exception;

import kakao.rebit.common.exception.BusinessException;

public class S3DownloadTimeOutErrorException extends BusinessException {

    public static final BusinessException EXCEPTION = new S3DownloadTimeOutErrorException();

    private S3DownloadTimeOutErrorException() {
        super(S3ErrorCode.S3_DOWNLOAD_TIMEOUT_ERROR);
    }
}
