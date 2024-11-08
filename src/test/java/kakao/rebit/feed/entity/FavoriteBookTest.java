package kakao.rebit.feed.entity;

import kakao.rebit.book.entity.Book;
import kakao.rebit.book.fixture.BookFixture;
import kakao.rebit.feed.fixture.FeedFixture;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.fixture.MemberFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class FavoriteBookTest {

    @Test
    void 인생책_텍스트_필드_수정() {
        // given
        Member author = MemberFixture.createDefault();
        Book book = BookFixture.createDefault();
        FavoriteBook favoriteBook = FeedFixture.createFavoriteBook(author, book);

        // when
        favoriteBook.updateTextFields("한줄평 업데이트", "서평 업데이트");

        // then
        Assertions.assertThat(favoriteBook.getBriefReview()).isEqualTo("한줄평 업데이트");
        Assertions.assertThat(favoriteBook.getFullReview()).isEqualTo("서평 업데이트");
    }

    @Test
    void 인생책_책_수정_성공() {
        // given
        Member author = MemberFixture.createDefault();
        Book book = BookFixture.createDefault();
        FavoriteBook favoriteBook = FeedFixture.createFavoriteBook(author, book);
        Book newBook = BookFixture.createUpdateBook();

        // when
        favoriteBook.changeBook(newBook);

        // then
        Assertions.assertThat(favoriteBook.getBook().getIsbn()).isEqualTo(newBook.getIsbn());
    }
}
