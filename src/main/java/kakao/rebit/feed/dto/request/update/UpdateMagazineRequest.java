package kakao.rebit.feed.dto.request.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import kakao.rebit.common.domain.ImageKeyHolder;

public record UpdateMagazineRequest(
        Long bookId,
        @Pattern(regexp = ImageKeyHolder.IMAGE_KEY_FORMAT, message = "피드 imageKey는 'feed/%s/%s' 형식이어야 합니다.")
        String imageKey,
        @NotBlank(message = "메거진 대상 이름은 필수 입력 값입니다.")
        String name,
        @NotBlank(message = "본문은 필수 입력 값입니다.")
        String content
) {

}
