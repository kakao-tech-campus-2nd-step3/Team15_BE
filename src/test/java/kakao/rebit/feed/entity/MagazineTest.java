package kakao.rebit.feed.entity;

import static org.assertj.core.api.Assertions.assertThat;

import kakao.rebit.book.entity.Book;
import kakao.rebit.book.fixture.BookFixture;
import kakao.rebit.feed.fixture.FeedFixture;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.fixture.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("매거진 테스트")
class MagazineTest {

    @Test
    void 매거진_텍스트_필드_수정() {
        // given
        Member author = MemberFixture.createDefault();
        Book book = BookFixture.createDefault();
        Magazine magazine = FeedFixture.createMagazine(author, book);

        // when
        magazine.updateTextFields("변경할 이름", "변경할 컨텐츠");

        // then
        assertThat(magazine.getName()).isEqualTo("변경할 이름");
        assertThat(magazine.getContent()).isEqualTo("변경할 컨텐츠");
    }

    @Test
    void 매거진_이미지_변경_여부_확인() {
        // given
        Member author = MemberFixture.createDefault();
        Book book = BookFixture.createDefault();
        Magazine magazine = FeedFixture.createMagazine(author, book);

        // when
        boolean result = magazine.isImageKeyUpdated("변경할 이미지");

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 이미지가_수정되지_않은_경우_매거진_이미지_변경_여부_확인() {
        // given
        Member author = MemberFixture.createDefault();
        Book book = BookFixture.createDefault();
        Magazine magazine = FeedFixture.createMagazine(author, book);

        // when
        boolean result = magazine.isImageKeyUpdated(magazine.getImageKey());

        // then
        assertThat(result).isFalse();
    }

    @Test
    void 매거진_이미지_수정() {
        // given
        Member author = MemberFixture.createDefault();
        Book book = BookFixture.createDefault();
        Magazine magazine = FeedFixture.createMagazine(author, book);

        // when
        magazine.changeImageKey("변경할 이미지");

        // then
        assertThat(magazine.getImageKey()).isEqualTo("변경할 이미지");
    }

    @Test
    void 매거진의_책_수정_성공() {
        // given
        Member author = MemberFixture.createDefault();
        Book book = BookFixture.createDefault();
        Magazine magazine = FeedFixture.createMagazine(author, book);
        Book newBook = BookFixture.createUpdateBook();

        // when
        magazine.changeBook(newBook);

        // then
        assertThat(magazine.getBook().getIsbn()).isEqualTo(newBook.getIsbn());
    }

    @Test
    void 매거진의_책_삭제() {
        // given
        Member author = MemberFixture.createDefault();
        Book book = BookFixture.createDefault();
        Magazine magazine = FeedFixture.createMagazine(author, book);

        // when
        magazine.changeBook(null);

        // then
        assertThat(magazine.getBook()).isNull();
    }
}
