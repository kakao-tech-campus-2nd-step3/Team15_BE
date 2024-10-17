package kakao.rebit.s3.service;

import java.time.Duration;
import kakao.rebit.s3.domain.S3Type;
import kakao.rebit.s3.dto.DownloadImageInfo;
import kakao.rebit.s3.dto.S3DownloadUrlResponse;
import kakao.rebit.s3.dto.S3UploadFileInfo;
import kakao.rebit.s3.dto.S3UploadKeyRequest;
import kakao.rebit.s3.dto.S3UploadUrlResponse;
import kakao.rebit.s3.exception.S3DeleteClientErrorException;
import kakao.rebit.s3.exception.S3DeleteErrorException;
import kakao.rebit.s3.exception.S3DeleteSdkErrorException;
import kakao.rebit.s3.exception.S3DeleteUnknownErrorException;
import kakao.rebit.s3.exception.S3DownloadClientErrorException;
import kakao.rebit.s3.exception.S3DownloadErrorException;
import kakao.rebit.s3.exception.S3DownloadSdkErrorException;
import kakao.rebit.s3.exception.S3DownloadUnknownErrorException;
import kakao.rebit.s3.exception.S3UploadClientErrorException;
import kakao.rebit.s3.exception.S3UploadErrorException;
import kakao.rebit.s3.exception.S3UploadSdkErrorException;
import kakao.rebit.s3.exception.S3UploadUnknownErrorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
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

    private final S3Presigner s3Presigner;
    private final S3Client s3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public S3Service(S3Presigner s3Presigner, S3Client s3Client) {
        this.s3Presigner = s3Presigner;
        this.s3Client = s3Client;
    }

    public S3UploadUrlResponse getUploadUrl(S3Type type, String fullFilename) {
        S3UploadFileInfo s3UploadFileInfo = S3UploadFileInfo.from(fullFilename); // 확장자를 포함한 전체 파일이름에서 이름과 확장자를 분리하기
        s3UploadFileInfo.validUploadImageFileFormat(); // 확장자를 이용해 사용 가능한 이미지 형식인지 검증

        // S3에 업로드할 객체 요청 생성
        S3UploadKeyRequest s3UploadUrlRequest = S3UploadKeyRequest.from(type, s3UploadFileInfo);

        try {
            PutObjectRequest putObjectRequest = createPutObjectRequest(
                    s3UploadUrlRequest.imageKey(),
                    s3UploadUrlRequest.contentType());

            // presigned URL의 유효 기간을 설정
            PutObjectPresignRequest putObjectPresignRequest = createPutObjectPresignRequest(putObjectRequest);

            // S3 presigner를 사용하여 실제 presigned URL 생성
            PresignedPutObjectRequest presignedPutObjectRequest = s3Presigner.presignPutObject(putObjectPresignRequest);

            String presignedUrl = presignedPutObjectRequest.url().toString();

            return createS3UploadUrlResponse(presignedUrl, s3UploadUrlRequest.imageKey());
        } catch (S3Exception e) {
            throw S3UploadErrorException.EXCEPTION;
        } catch (SdkClientException e) {
            throw S3UploadClientErrorException.EXCEPTION;
        } catch (SdkException e) {
            throw S3UploadSdkErrorException.EXCEPTION;
        } catch (Exception e) {
            throw S3UploadUnknownErrorException.EXCEPTION;
        }
    }

    public S3DownloadUrlResponse getDownloadUrl(String imageKey) {
        try {
            GetObjectRequest getObjectRequest = createGetObjectRequest(imageKey);

            GetObjectPresignRequest getObjectPresignRequest = createGetObjectPresignRequest(getObjectRequest);

            PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest);

            String presignedUrl = presignedGetObjectRequest.url().toString();

            return createS3DownloadUrlResponse(presignedUrl);
        } catch (S3Exception e) {
            throw S3DownloadErrorException.EXCEPTION;
        } catch (SdkClientException e) {
            throw S3DownloadClientErrorException.EXCEPTION;
        } catch (SdkException e) {
            throw S3DownloadSdkErrorException.EXCEPTION;
        } catch (Exception e) {
            throw S3DownloadUnknownErrorException.EXCEPTION;
        }
    }

    public void deleteObject(String imageKey) {
        try {
            DeleteObjectRequest deleteObjectRequest = createDeleteObjectRequest(imageKey);
            s3Client.deleteObject(deleteObjectRequest);
        } catch (S3Exception e) {
            throw S3DeleteErrorException.EXCEPTION;
        } catch (SdkClientException e) {
            throw S3DeleteClientErrorException.EXCEPTION;
        } catch (SdkException e) {
            throw S3DeleteSdkErrorException.EXCEPTION;
        } catch (Exception e) {
            throw S3DeleteUnknownErrorException.EXCEPTION;
        }
    }

    /**
     * 다운받은 이미지를 파라미터로 받아서 S3에 업로드하는 메서드. 최초 로그인 시 카카오에서 받아온 프로필 이미지를 추출하여 S3에 저장에 때 사용
     */
    public void putObject(S3UploadKeyRequest s3UploadKeyRequest,
            DownloadImageInfo downloadImageInfo) {
        try {
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .contentType(s3UploadKeyRequest.contentType())
                    .key(s3UploadKeyRequest.imageKey())
                    .build();

            s3Client.putObject(
                    objectRequest,
                    RequestBody.fromBytes(downloadImageInfo.imageBytes())
            );

        } catch (S3Exception e) {
            throw S3DeleteErrorException.EXCEPTION;
        } catch (SdkClientException e) {
            throw S3DeleteClientErrorException.EXCEPTION;
        } catch (SdkException e) {
            throw S3DeleteSdkErrorException.EXCEPTION;
        } catch (Exception e) {
            throw S3DeleteUnknownErrorException.EXCEPTION;
        }
    }

    /**
     * 업로드를 위한 PresignedUrl 생성
     */
    private PutObjectRequest createPutObjectRequest(String imageKey, String contentType) {
        return PutObjectRequest.builder() // S3에 업로드할 파일의 요청을 생성
                .bucket(bucket)
                .key(imageKey)
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
    private DeleteObjectRequest createDeleteObjectRequest(String imageKey) {
        return DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(imageKey)
                .build();
    }

    private S3UploadUrlResponse createS3UploadUrlResponse(String presignedUrl, String imageKey) {
        return new S3UploadUrlResponse(presignedUrl, imageKey);
    }

    private S3DownloadUrlResponse createS3DownloadUrlResponse(String presignedUrl) {
        return new S3DownloadUrlResponse(presignedUrl);
    }
}
