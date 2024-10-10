package kakao.rebit.s3.exception;

import kakao.rebit.common.exception.BusinessException;

public class S3DownloadClientErrorException extends BusinessException {

    public static final BusinessException EXCEPTION = new S3DownloadClientErrorException();

    private S3DownloadClientErrorException() {
        super(S3ErrorCode.S3_DOWNLOAD_CLIENT_ERROR);
    }
}
