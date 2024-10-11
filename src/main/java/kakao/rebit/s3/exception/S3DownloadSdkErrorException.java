package kakao.rebit.s3.exception;

import kakao.rebit.common.exception.BusinessException;

public class S3DownloadSdkErrorException extends BusinessException {

    public static final BusinessException EXCEPTION = new S3DownloadSdkErrorException();

    private S3DownloadSdkErrorException() {
        super(S3ErrorCode.S3_DOWNLOAD_SDK_ERROR);
    }
}
