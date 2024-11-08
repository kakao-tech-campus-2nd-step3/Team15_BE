package kakao.rebit.feed.entity;

import kakao.rebit.book.entity.Book;
import kakao.rebit.book.fixture.BookFixture;
import kakao.rebit.feed.fixture.FeedFixture;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.fixture.MemberFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MagazineTest {

    @Test
    void 메거진_텍스트_필드_수정() {
        // given
        Member author = MemberFixture.createDefault();
        Book book = BookFixture.createDefault();
        Magazine magazine = FeedFixture.createMagazine(author, book);

        // when
        magazine.updateTextFields("변경할 이름", "변경할 컨텐츠");

        // then
        Assertions.assertThat(magazine.getName()).isEqualTo("변경할 이름");
        Assertions.assertThat(magazine.getContent()).isEqualTo("변경할 컨텐츠");
    }

    @Test
    void 메거진_이미지_변경_여부_확인() {
        // given
        Member author = MemberFixture.createDefault();
        Book book = BookFixture.createDefault();
        Magazine magazine = FeedFixture.createMagazine(author, book);

        // when
        boolean result = magazine.isImageKeyUpdated("변경할 이미지");

        // then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    void 이미지가_수정되지_않은_경우_메거진_이미지_변경_여부_확인() {
        // given
        Member author = MemberFixture.createDefault();
        Book book = BookFixture.createDefault();
        Magazine magazine = FeedFixture.createMagazine(author, book);

        // when
        boolean result = magazine.isImageKeyUpdated(magazine.getImageKey());

        // then
        Assertions.assertThat(result).isFalse();
    }

    @Test
    void 메거진_이미지_수정() {
        // given
        Member author = MemberFixture.createDefault();
        Book book = BookFixture.createDefault();
        Magazine magazine = FeedFixture.createMagazine(author, book);

        // when
        magazine.changeImageKey("변경할 이미지");

        // then
        Assertions.assertThat(magazine.getImageKey()).isEqualTo("변경할 이미지");
    }

    @Test
    void 메거진의_책_수정_성공() {
        // given
        Member author = MemberFixture.createDefault();
        Book book = BookFixture.createDefault();
        Magazine magazine = FeedFixture.createMagazine(author, book);
        Book newBook = BookFixture.createUpdateBook();

        // when
        magazine.changeBook(newBook);

        // then
        Assertions.assertThat(magazine.getBook().getIsbn()).isEqualTo(newBook.getIsbn());
    }

    @Test
    void 메거진의_책_삭제() {
        // given
        Member author = MemberFixture.createDefault();
        Book book = BookFixture.createDefault();
        Magazine magazine = FeedFixture.createMagazine(author, book);

        // when
        magazine.changeBook(null);

        // then
        Assertions.assertThat(magazine.getBook()).isNull();
    }
}
