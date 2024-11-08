package kakao.rebit.feed.entity;

import static org.assertj.core.api.Assertions.assertThat;

import kakao.rebit.book.entity.Book;
import kakao.rebit.book.fixture.BookFixture;
import kakao.rebit.feed.fixture.FeedFixture;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.fixture.MemberFixture;
import org.junit.jupiter.api.Test;

public class CommonFeedTest {

    @Test
    void 피드_작성자_확인_성공() {
        // given
        Member author = MemberFixture.createDefault();
        Book book = BookFixture.createDefault();

        FavoriteBook favoriteBook = FeedFixture.createFavoriteBook(author, book);
        Magazine magazine = FeedFixture.createMagazine(author, book);
        Story story = FeedFixture.createStory(author, book);

        // when
        boolean resultToFavoriteBook = favoriteBook.isWrittenBy(author);
        boolean resultToMagazine = magazine.isWrittenBy(author);
        boolean resultToStory = story.isWrittenBy(author);

        // then
        assertThat(resultToFavoriteBook).isTrue();
        assertThat(resultToMagazine).isTrue();
        assertThat(resultToStory).isTrue();
    }

    @Test
    void 다른_사용자가_작성자가_아님을_검증() {
        // given
        Member author = MemberFixture.createDefault();
        Member viewer = MemberFixture.createCustomUser("another user");
        Book book = BookFixture.createDefault();

        FavoriteBook favoriteBook = FeedFixture.createFavoriteBook(author, book);
        Magazine magazine = FeedFixture.createMagazine(author, book);
        Story story = FeedFixture.createStory(author, book);

        // when
        boolean resultToFavoriteBook = favoriteBook.isWrittenBy(viewer);
        boolean resultToMagazine = magazine.isWrittenBy(viewer);
        boolean resultToStory = story.isWrittenBy(viewer);

        // then
        assertThat(resultToFavoriteBook).isFalse();
        assertThat(resultToMagazine).isFalse();
        assertThat(resultToStory).isFalse();
    }
}
