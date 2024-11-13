package kakao.rebit.diary.dto;

public record DiaryResponse(
        Long id,
        String content,
        Long memberId,
        DiaryBookResponse book,
        String date
) {

}
