package kakao.rebit.s3.dto;

import java.util.Arrays;
import java.util.List;
import kakao.rebit.s3.exception.S3NotAllowedFileFormatException;
import kakao.rebit.utils.file.FileUtil;

public record S3UploadFileInfo(
        String filename,
        String extension
) {

    private static final List<String> ALLOWED_FILE_FORMAT = Arrays.asList("jpg", "jpeg", "png", "gif", "svg", "webp");

    public S3UploadFileInfo {
        validUploadImageFileFormat(extension);
    }

    private void validUploadImageFileFormat(String extension) {
        if (!ALLOWED_FILE_FORMAT.contains(extension)) {
            throw S3NotAllowedFileFormatException.EXCEPTION;
        }
    }

    public static S3UploadFileInfo from(String fullFileName) {
        return new S3UploadFileInfo(
                FileUtil.extractFilename(fullFileName),
                FileUtil.extractExtension(fullFileName)
        );
    }
}
