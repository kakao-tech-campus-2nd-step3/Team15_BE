package kakao.rebit.diary.dto;

import jakarta.validation.constraints.NotBlank;

public record DiaryRequest(
    @NotBlank(message = "일기 내용은 필수입니다.") String content,
    @NotBlank(message = "ISBN 값은 필수입니다.") String isbn
) {}
