package kakao.rebit.feed.dto.request.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import kakao.rebit.common.domain.ImageKeyModifier;

public record UpdateStoryRequest(
        Long bookId,

        @NotBlank(message = "이미지는 필수입니다.")
        @Pattern(regexp = "^feed" + ImageKeyModifier.BASE_IMAGE_KEY_FORMAT, message = "피드 imageKey는 'feed/%s/%s' 형식이어야 합니다.")
        String imageKey,

        @NotBlank(message = "본문은 필수 입력 값입니다.")
        String content
) {

}
