package kakao.rebit.s3.service;

import java.time.Duration;
import java.util.UUID;
import kakao.rebit.s3.dto.DownloadResponse;
import kakao.rebit.s3.dto.UploadResponse;
import kakao.rebit.s3.exception.S3ErrorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
public class S3Service {

    private static final String KEY = "feed/%s/%s";
    private static final String CONTENT_TYPE = "image/%s";

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final S3Presigner s3Presigner;
    private final S3Client s3Client;

    public S3Service(S3Presigner s3Presigner, S3Client s3Client) {
        this.s3Presigner = s3Presigner;
        this.s3Client = s3Client;
    }

    public UploadResponse getUploadUrl(String fullFilename) {
        String filename = getFilename(fullFilename);
        String extension = getExtension(fullFilename);

        // S3에 업로드할 객체 요청 생성
        String key = createKey(filename);
        String contentType = createContentType(extension);

        try {
            PutObjectRequest putObjectRequest = getPutObjectRequest(key, contentType);

            // presigned URL의 유효 기간을 설정
            PutObjectPresignRequest putObjectPresignRequest = getPutObjectPresignRequest(
                    putObjectRequest);

            // S3 presigner를 사용하여 실제 presigned URL 생성
            PresignedPutObjectRequest presignedPutObjectRequest = s3Presigner.presignPutObject(
                    putObjectPresignRequest);

            String presignedUrl = presignedPutObjectRequest.url().toString();

            return createUploadResponse(presignedUrl, key);
        } catch (S3Exception e) {
            throw S3ErrorException.EXCEPTION;
        }
    }

    public DownloadResponse getDownloadUrl(String key) {
        try {
            GetObjectRequest getObjectRequest = getGetObjectRequest(key);

            GetObjectPresignRequest getObjectPresignRequest = getGetObjectPresignRequest(
                    getObjectRequest);

            PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(
                    getObjectPresignRequest);

            String presignedUrl = presignedGetObjectRequest.url().toString();

            return createDownloadResponse(presignedUrl);
        } catch (S3Exception e) {
            throw S3ErrorException.EXCEPTION;
        }
    }

    @Transactional
    public void deleteObject(String key) {
        DeleteObjectRequest deleteObjectRequest = getDeleteObjectRequest(key);
        try {
            s3Client.deleteObject(deleteObjectRequest);
        } catch (S3Exception e) {
            throw S3ErrorException.EXCEPTION;
        }
    }

    /**
     * 업로드를 위한 PresignedUrl 생성
     */
    private PutObjectRequest getPutObjectRequest(String key, String contentType) {
        return PutObjectRequest.builder() // S3에 업로드할 파일의 요청을 생성
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .build();
    }

    private PutObjectPresignRequest getPutObjectPresignRequest(PutObjectRequest putObjectRequest) {
        return PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5)) // presigned URL 5분간 접근 허용
                .putObjectRequest(putObjectRequest)
                .build();
    }

    /**
     * 다운로드를 위한 PresignedUrl 생성
     */
    private GetObjectRequest getGetObjectRequest(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        return getObjectRequest;
    }

    private GetObjectPresignRequest getGetObjectPresignRequest(GetObjectRequest getObjectRequest) {
        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .getObjectRequest(getObjectRequest)
                .build();
        return getObjectPresignRequest;
    }

    /**
     * 삭제를 위한 Request 생성
     */
    private DeleteObjectRequest getDeleteObjectRequest(String key) {
        return DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
    }

    /**
     * key 생성
     */
    private String createKey(String filename) {
        return String.format(KEY, UUID.randomUUID(), filename);
    }

    private String createContentType(String extension) {
        return String.format(CONTENT_TYPE, extension);
    }

    /**
     * 파일의 확장자 앞 이름 얻는 메서드
     */
    private String getFilename(String fullFileName) {
        return fullFileName.substring(0, fullFileName.lastIndexOf("."));
    }

    /**
     * 파일의 확장자 얻는 메서드
     */
    private String getExtension(String fullFileName) {
        return fullFileName.substring(fullFileName.lastIndexOf(".") + 1);
    }

    private UploadResponse createUploadResponse(String presignedUrl, String key) {
        return new UploadResponse(presignedUrl, key);
    }

    private DownloadResponse createDownloadResponse(String presignedUrl) {
        return new DownloadResponse(presignedUrl);
    }
}
