package kakao.rebit.s3.exception;

import kakao.rebit.common.exception.BusinessException;

public class S3UploadUnknownErrorException extends BusinessException{
    public static final BusinessException EXCEPTION = new S3UploadUnknownErrorException();

    private S3UploadUnknownErrorException(){
        super(S3ErrorCode.S3_UPLOAD_UNKNOWN_ERROR);
    }
}
