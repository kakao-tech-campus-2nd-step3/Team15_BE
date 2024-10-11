package kakao.rebit.s3.exception;

import kakao.rebit.common.exception.BusinessException;

public class S3DownloadErrorException extends BusinessException {

    public static final BusinessException EXCEPTION = new S3DownloadErrorException();

    private S3DownloadErrorException() {
        super(S3ErrorCode.S3_DOWNLOAD_ERROR);
    }
}
