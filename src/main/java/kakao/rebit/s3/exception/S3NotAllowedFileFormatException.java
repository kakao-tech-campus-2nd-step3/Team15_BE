package kakao.rebit.s3.exception;

import kakao.rebit.common.exception.BusinessException;

public class S3NotAllowedFileFormatException extends BusinessException {

    public static final BusinessException EXCEPTION = new S3NotAllowedFileFormatException();

    private S3NotAllowedFileFormatException() {
        super(S3ErrorCode.S3_NOT_ALLOWED_FILE_FORMAT);
    }
}
