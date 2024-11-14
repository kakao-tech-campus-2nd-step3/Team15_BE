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
        // given
        Book newBook = BookFixture.createBookWithIsbn("9876543210");
        String newContent = "수정된 내용";

        // when
        diary.updateDiary(newContent, newBook);

        // then
        assertEquals(newContent, diary.getContent());
        assertEquals(newBook, diary.getBook());
    }
}
