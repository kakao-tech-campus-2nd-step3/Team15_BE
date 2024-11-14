package kakao.rebit.diary.entity;

import kakao.rebit.book.entity.Book;
import kakao.rebit.member.entity.Member;
import kakao.rebit.diary.fixture.DiaryFixture;
import kakao.rebit.book.fixture.BookFixture;
import kakao.rebit.member.fixture.MemberFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiaryTest {

    private Member member;
    private Book book;
    private Diary diary;

    @BeforeEach
    void setUp() {
        member = MemberFixture.createDefault();
        book = BookFixture.createDefault();
        diary = DiaryFixture.createDefaultDiary(member, book);
    }

    @Test
    void 다이어리_내용_수정_성공() {
        // 새로운 book 생성 후 다이어리 내용과 책을 업데이트
        Book newBook = BookFixture.createBookWithIsbn("9876543210");
        String newContent = "수정된 내용";

        diary.updateDiary(newContent, newBook);

        // 업데이트 후 변경 사항이 적용되었는지 확인
        assertEquals(newContent, diary.getContent());
        assertEquals(newBook, diary.getBook());
    }
}
