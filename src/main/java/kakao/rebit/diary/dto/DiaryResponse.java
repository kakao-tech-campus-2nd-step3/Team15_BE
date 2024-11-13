package kakao.rebit.diary.dto;

import java.time.LocalDateTime;

public record DiaryResponse(Long id, String content, Long memberId, String isbn, LocalDateTime createAt) {}
