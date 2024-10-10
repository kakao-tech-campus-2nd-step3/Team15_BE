package kakao.rebit.s3.exception;

import kakao.rebit.common.exception.BusinessException;

public class S3DownloadUnknownErrorException extends BusinessException{
    public static final BusinessException EXCEPTION = new S3DownloadUnknownErrorException();

    private S3DownloadUnknownErrorException(){
        super(S3ErrorCode.S3_DOWNLOAD_UNKNOWN_ERROR);
    }
}
