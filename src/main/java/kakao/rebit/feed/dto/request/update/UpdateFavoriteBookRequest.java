package kakao.rebit.feed.dto.request.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateFavoriteBookRequest(
        @NotNull(message = "인생책에서 책은 필수 입력 값입니다.")
        Long bookId,
        @NotBlank(message = "한줄평은 필수 입력 값입니다.")
        String briefReview,
        @NotBlank(message = "서평은 필수 입력 값입니다.")
        String fullReview) {

}
