package kakao.rebit.auth.fixture;

import kakao.rebit.s3.domain.S3Type;
import kakao.rebit.s3.dto.DownloadImageInfo;
import kakao.rebit.s3.dto.S3DownloadUrlResponse;
import kakao.rebit.s3.dto.S3UploadKeyRequest;
import kakao.rebit.s3.dto.S3UploadUrlResponse;
import kakao.rebit.s3.service.S3Service;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Primary
@Profile("test")
public class TestS3Service extends S3Service {

    private static final String TEST_PRESIGNED_URL = "https://test-bucket.s3.amazonaws.com/test-image.jpg";

    public TestS3Service() {
        super(null, null); // 실제 S3 클라이언트는 사용하지 않음
    }

    @Override
    public S3UploadUrlResponse getUploadUrl(S3Type type, String fullFilename) {
        S3UploadKeyRequest request = createS3UploadKeyRequestFromTypeAndFilename(type, fullFilename);
        return new S3UploadUrlResponse(TEST_PRESIGNED_URL, request.imageKey());
    }

    @Override
    public S3DownloadUrlResponse getDownloadUrl(String imageKey) {
        return new S3DownloadUrlResponse(TEST_PRESIGNED_URL);
    }

    @Override
    public void deleteObject(String imageKey) {
        // 테스트 환경에서는 아무 동작도 하지 않음
    }

    @Override
    public void putObject(S3UploadKeyRequest s3UploadKeyRequest, DownloadImageInfo downloadImageInfo) {
        // 테스트 환경에서는 아무 동작도 하지 않음
    }

    @Override
    public S3UploadKeyRequest createS3UploadKeyRequestFromTypeAndFilename(S3Type type, String filename) {
        return super.createS3UploadKeyRequestFromTypeAndFilename(type, filename);
    }
}
