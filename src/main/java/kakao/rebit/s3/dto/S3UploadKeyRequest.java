package kakao.rebit.s3.dto;

import java.util.UUID;
import kakao.rebit.s3.domain.S3Type;

public record S3UploadKeyRequest(
        String imageKey,
        String contentType
) {

    private static final String KEY = "%s/%s/%s";
    private static final String CONTENT_TYPE = "image/%s";

    public static S3UploadKeyRequest from(S3Type type, S3UploadFileInfo fileInfo){
        return new S3UploadKeyRequest(
                createKey(type, fileInfo.filename()),
                createContentType(fileInfo.extension())
        );
    }

    public static String createKey(S3Type type, String filename) {
        return String.format(KEY, type.toString().toLowerCase(), UUID.randomUUID(), filename);
    }

    public static String createContentType(String extension) {
        return String.format(CONTENT_TYPE, extension);
    }
}
