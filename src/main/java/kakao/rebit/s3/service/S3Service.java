package kakao.rebit.s3.service;

import java.time.Duration;
import java.util.UUID;
import kakao.rebit.s3.dto.S3DownloadUrlResponse;
import kakao.rebit.s3.dto.S3UploadUrlResponse;
import kakao.rebit.s3.exception.S3DeleteClientErrorException;
import kakao.rebit.s3.exception.AwsDeleteServiceErrorException;
import kakao.rebit.s3.exception.S3DeleteServiceErrorException;
import kakao.rebit.s3.exception.S3DeleteTimeOutErrorException;
import kakao.rebit.s3.exception.S3DeleteUnknownErrorException;
import kakao.rebit.s3.exception.S3DownloadClientErrorException;
import kakao.rebit.s3.exception.AwsDownloadServiceErrorException;
import kakao.rebit.s3.exception.S3DownloadServiceErrorException;
import kakao.rebit.s3.exception.S3DownloadTimeOutErrorException;
import kakao.rebit.s3.exception.S3DownloadUnknownErrorException;
import kakao.rebit.s3.exception.S3UploadClientErrorException;
import kakao.rebit.s3.exception.AwsUploadServiceErrorException;
import kakao.rebit.s3.exception.S3UploadServiceErrorException;
import kakao.rebit.s3.exception.S3UploadTimeOutErrorException;
import kakao.rebit.s3.exception.S3UploadUnknownErrorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.exception.SdkServiceException;
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

    public S3UploadUrlResponse getUploadUrl(String fullFilename) {
        String filename = getFilename(fullFilename);
        String extension = getExtension(fullFilename);

        // S3에 업로드할 객체 요청 생성
        String key = createKey(filename);
        String contentType = createContentType(extension);

        try {
            PutObjectRequest putObjectRequest = createPutObjectRequest(key, contentType);

            // presigned URL의 유효 기간을 설정
            PutObjectPresignRequest putObjectPresignRequest = createPutObjectPresignRequest(
                    putObjectRequest);

            // S3 presigner를 사용하여 실제 presigned URL 생성
            PresignedPutObjectRequest presignedPutObjectRequest = s3Presigner.presignPutObject(
                    putObjectPresignRequest);

            String presignedUrl = presignedPutObjectRequest.url().toString();

            return createS3UploadUrlResponse(presignedUrl, key);
        } catch (S3Exception e) {
            throw S3UploadServiceErrorException.EXCEPTION;
        } catch (SdkClientException e) {
            // 클라이언트 측에서 발생하는 예외 처리 (예: 요청 오류)
            throw S3UploadClientErrorException.EXCEPTION;
        } catch (SdkServiceException e) {
            // AWS 서비스에서 발생하는 일반적인 예외 처리 (예: 서비스 요청 실패)
            throw AwsUploadServiceErrorException.EXCEPTION;
        } catch (SdkException e) {
            // 기타 SDK 예외 처리 (예: 요청 타임아웃 등)
            throw S3UploadTimeOutErrorException.EXCEPTION;
        } catch (Exception e) {
            throw S3UploadUnknownErrorException.EXCEPTION;
        }
    }

    public S3DownloadUrlResponse getDownloadUrl(String key) {
        try {
            GetObjectRequest getObjectRequest = createGetObjectRequest(key);

            GetObjectPresignRequest getObjectPresignRequest = createGetObjectPresignRequest(
                    getObjectRequest);

            PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(
                    getObjectPresignRequest);

            String presignedUrl = presignedGetObjectRequest.url().toString();

            return createS3DownloadUrlResponse(presignedUrl);
        } catch (S3Exception e) {
            throw S3DownloadServiceErrorException.EXCEPTION;
        } catch (SdkClientException e) {
            throw S3DownloadClientErrorException.EXCEPTION;
        } catch (SdkServiceException e) {
            throw AwsDownloadServiceErrorException.EXCEPTION;
        } catch (SdkException e) {
            throw S3DownloadTimeOutErrorException.EXCEPTION;
        } catch (Exception e) {
            throw S3DownloadUnknownErrorException.EXCEPTION;
        }
    }

    @Transactional
    public void deleteObject(String key) {
        DeleteObjectRequest deleteObjectRequest = createDeleteObjectRequest(key);
        try {
            s3Client.deleteObject(deleteObjectRequest);
        } catch (S3Exception e) {
            throw S3DeleteServiceErrorException.EXCEPTION;
        } catch (SdkClientException e) {
            throw S3DeleteClientErrorException.EXCEPTION;
        } catch (SdkServiceException e) {
            throw AwsDeleteServiceErrorException.EXCEPTION;
        } catch (SdkException e) {
            throw S3DeleteTimeOutErrorException.EXCEPTION;
        } catch (Exception e) {
            throw S3DeleteUnknownErrorException.EXCEPTION;
        }
    }

    /**
     * 업로드를 위한 PresignedUrl 생성
     */
    private PutObjectRequest createPutObjectRequest(String key, String contentType) {
        return PutObjectRequest.builder() // S3에 업로드할 파일의 요청을 생성
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .build();
    }

    private PutObjectPresignRequest createPutObjectPresignRequest(
            PutObjectRequest putObjectRequest) {
        return PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5)) // presigned URL 5분간 접근 허용
                .putObjectRequest(putObjectRequest)
                .build();
    }

    /**
     * 다운로드를 위한 PresignedUrl 생성
     */
    private GetObjectRequest createGetObjectRequest(String key) {
        return GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
    }

    private GetObjectPresignRequest createGetObjectPresignRequest(
            GetObjectRequest getObjectRequest) {
        return GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .getObjectRequest(getObjectRequest)
                .build();
    }

    /**
     * 삭제를 위한 Request 생성
     */
    private DeleteObjectRequest createDeleteObjectRequest(String key) {
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

    private S3UploadUrlResponse createS3UploadUrlResponse(String presignedUrl, String key) {
        return new S3UploadUrlResponse(presignedUrl, key);
    }

    private S3DownloadUrlResponse createS3DownloadUrlResponse(String presignedUrl) {
        return new S3DownloadUrlResponse(presignedUrl);
    }
}
