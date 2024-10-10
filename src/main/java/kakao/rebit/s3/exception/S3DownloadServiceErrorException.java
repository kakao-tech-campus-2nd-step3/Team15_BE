package kakao.rebit.s3.exception;

import kakao.rebit.common.exception.BusinessException;

public class S3DownloadServiceErrorException extends BusinessException{
    public static final BusinessException EXCEPTION = new S3DownloadServiceErrorException();

    private S3DownloadServiceErrorException(){
        super(S3ErrorCode.S3_DOWNLOAD_SERVICE_ERROR);
    }
}
