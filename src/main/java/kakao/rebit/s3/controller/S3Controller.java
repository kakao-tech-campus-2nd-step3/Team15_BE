package kakao.rebit.s3.controller;

import kakao.rebit.s3.dto.S3UploadUrlResponse;
import kakao.rebit.s3.service.S3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/s3/urls")
public class S3Controller {

    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping("/upload")
    public ResponseEntity<S3UploadUrlResponse> getUploadS3Url(
            @RequestParam("filename") String filename) {
        S3UploadUrlResponse s3UploadUrlResponse = s3Service.getUploadUrl(filename);
        return ResponseEntity.ok().body(s3UploadUrlResponse);
    }
}
