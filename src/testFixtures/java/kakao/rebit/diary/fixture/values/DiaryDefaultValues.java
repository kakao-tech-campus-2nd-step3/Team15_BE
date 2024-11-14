package kakao.rebit.diary.fixture.values;

import java.time.LocalDate;

public record DiaryDefaultValues(
        String content,
        String isbn,
        LocalDate date
) {

    public static final DiaryDefaultValues INSTANCE = new DiaryDefaultValues(
            "독서일기 default 컨텐츠",
            "9788937460784",
            LocalDate.of(2024, 11, 11)
    );
}
