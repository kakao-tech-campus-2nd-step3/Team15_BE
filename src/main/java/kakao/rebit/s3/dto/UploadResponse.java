package kakao.rebit.s3.dto;

public record UploadResponse(
        String presignedUrl,
        String key
) {

}
