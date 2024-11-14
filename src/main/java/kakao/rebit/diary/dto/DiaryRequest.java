package kakao.rebit.diary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DiaryRequest(
        @NotBlank(message = "일기 내용은 필수입니다.")
        String content,

        @NotBlank(message = "ISBN 값은 필수입니다.")
        String isbn,

        @NotBlank(message = "날짜 값은 필수입니다.")
        @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$", message = "날짜는 YYYY-MM-DD 형식입니다.")
        String date
) {

}
