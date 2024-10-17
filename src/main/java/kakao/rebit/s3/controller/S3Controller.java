package kakao.rebit.s3.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kakao.rebit.s3.domain.S3Type;
import kakao.rebit.s3.dto.S3UploadUrlResponse;
import kakao.rebit.s3.service.S3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/s3/urls")
@Tag(name = "3S API", description = "S3 관련 API")
public class S3Controller {

    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @Operation(summary = "업로드 PresignedUrl 발급", description = "업로드를 위한 PresignedUrl을 발급합니다.")
    @GetMapping("/upload")
    public ResponseEntity<S3UploadUrlResponse> getUploadS3Url(
            @RequestParam("type") S3Type type,
            @RequestParam("filename") String filename) {
        S3UploadUrlResponse s3UploadUrlResponse = s3Service.getUploadUrl(type, filename);
        return ResponseEntity.ok().body(s3UploadUrlResponse);
    }
}
