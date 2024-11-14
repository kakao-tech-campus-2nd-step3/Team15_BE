package kakao.rebit.feed.dto.request.update;

import static kakao.rebit.common.domain.ImageKeyModifier.BASE_IMAGE_KEY_FORMAT;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateMagazineRequest(
        Long bookId,

        @NotBlank(message = "이미지는 필수입니다.")
        @Pattern(regexp = "^feed" + BASE_IMAGE_KEY_FORMAT, message = "피드 imageKey는 'feed/UUID/filename' 형식이어야 합니다.")
        String imageKey,

        @NotBlank(message = "매거진 대상 이름은 필수 입력 값입니다.")
        String name,

        @NotBlank(message = "본문은 필수 입력 값입니다.")
        String content
) {

}
