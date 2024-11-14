package kakao.rebit.diary.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record DiaryRequest(
        @NotBlank(message = "일기 내용은 필수입니다.")
        String content,

        @NotBlank(message = "ISBN 값은 필수입니다.")
        String isbn,

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate date
) {

}
