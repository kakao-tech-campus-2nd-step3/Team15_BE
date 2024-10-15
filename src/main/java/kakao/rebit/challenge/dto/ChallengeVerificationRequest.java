package kakao.rebit.challenge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record ChallengeVerificationRequest(
        @NotBlank(message = "제목은 필수입니다.")
        @Size(min = 2, max = 100, message = "제목은 2자 이상 100자 이하여야 합니다.")
        String title,

        @NotBlank(message = "이미지 URL은 필수입니다.")
        @URL(message = "올바른 URL 형식이어야 합니다.")
        String imageUrl,

        @NotBlank(message = "내용은 필수입니다.")
        @Size(max = 1000, message = "내용은 1000자 이하여야 합니다.")
        String content
) {

}
