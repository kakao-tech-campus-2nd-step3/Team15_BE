package kakao.rebit.diary.fixture;

import java.time.LocalDate;
import kakao.rebit.book.entity.Book;
import kakao.rebit.diary.dto.DiaryRequest;
import kakao.rebit.diary.entity.Diary;
import kakao.rebit.diary.fixture.values.DiaryDefaultValues;
import kakao.rebit.member.entity.Member;

public class DiaryFixture {

    public static Diary createDiary(Member member, Book book, String content, LocalDate date) {
        return new Diary(content, member, book, date);
    }

    public static Diary createDefaultDiary(Member member, Book book) {
        return new Diary(
                DiaryDefaultValues.INSTANCE.content(),
                member,
                book,
                DiaryDefaultValues.INSTANCE.date());
    }

    public static DiaryRequest createDiaryRequestWithContentAndDate(String content, String isbn, LocalDate date) {
        return new DiaryRequest(
                content,
                isbn,
                date
        );
    }
}
