package kakao.rebit.common.exception;

public class ImageDownloadErrorException extends BusinessException {

    public static final BusinessException EXCEPTION = new ImageDownloadErrorException();

    private ImageDownloadErrorException() {
        super(ImageErrorCode.IMAGE_DOWNLOAD_ERROR);
    }
}
