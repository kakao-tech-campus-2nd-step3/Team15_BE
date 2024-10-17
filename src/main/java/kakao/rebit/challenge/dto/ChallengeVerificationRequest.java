package kakao.rebit.challenge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import kakao.rebit.common.domain.ImageKeyAccessor;

public record ChallengeVerificationRequest(
        @NotBlank(message = "제목은 필수입니다.")
        @Size(min = 2, max = 100, message = "제목은 2자 이상 100자 이하여야 합니다.")
        String title,

        @NotBlank(message = "이미지는 필수입니다.")
        @Pattern(regexp = "^challenge_verification" + ImageKeyAccessor.BASE_IMAGE_KEY_FORMAT, message = "챌린지 인증글 imageKey는 'challenge_verification/%s/%s' 형식이어야 합니다.")
        String imageKey,

        @NotBlank(message = "내용은 필수입니다.")
        @Size(max = 1000, message = "내용은 1000자 이하여야 합니다.")
        String content
) {

}
