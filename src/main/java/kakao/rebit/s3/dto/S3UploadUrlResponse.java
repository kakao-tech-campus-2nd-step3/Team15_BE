package kakao.rebit.s3.dto;

public record S3UploadUrlResponse(
        String presignedUrl,
        String key
) {

}
