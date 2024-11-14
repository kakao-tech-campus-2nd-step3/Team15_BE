package kakao.rebit.diary.fixture;

import kakao.rebit.diary.entity.Diary;
import kakao.rebit.member.entity.Member;
import kakao.rebit.book.entity.Book;

public class DiaryFixture {

    public static Diary createDiary(Member member, Book book, String content, String date) {
        return new Diary(content, member, book, date);
    }

    public static Diary createDefaultDiary(Member member, Book book) {
        return new Diary("기본 내용", member, book, "2024-11-13");
    }
}
