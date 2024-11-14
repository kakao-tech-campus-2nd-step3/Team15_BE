package kakao.rebit.diary.dto;

import java.time.LocalDate;

public record DiaryResponse(
        Long id,
        String content,
        Long memberId,
        DiaryBookResponse book,
        LocalDate date
) {

}
