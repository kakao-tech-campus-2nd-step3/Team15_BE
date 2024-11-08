package kakao.rebit.feed.entity;

import static org.assertj.core.api.Assertions.assertThat;

import kakao.rebit.book.entity.Book;
import kakao.rebit.book.fixture.BookFixture;
import kakao.rebit.feed.fixture.FeedFixture;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.fixture.MemberFixture;
import org.junit.jupiter.api.Test;

class StoryTest {

    @Test
    void 스토리_텍스트_필드_수정() {
        // given
        Member author = MemberFixture.createDefault();
        Book book = BookFixture.createDefault();
        Story Story = FeedFixture.createStory(author, book);

        // when
        Story.updateTextFields("변경할 컨텐츠");

        // then
        assertThat(Story.getContent()).isEqualTo("변경할 컨텐츠");
    }

    @Test
    void 스토리_이미지_변경_여부_확인() {
        // given
        Member author = MemberFixture.createDefault();
        Book book = BookFixture.createDefault();
        Story Story = FeedFixture.createStory(author, book);

        // when
        boolean result = Story.isImageKeyUpdated("변경할 이미지");

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 이미지가_수정되지__않은_경우_스토리_이미지_변경_여부_확인() {
        // given
        Member author = MemberFixture.createDefault();
        Book book = BookFixture.createDefault();
        Story Story = FeedFixture.createStory(author, book);

        // when
        boolean result = Story.isImageKeyUpdated(Story.getImageKey());

        // then
        assertThat(result).isFalse();
    }

    @Test
    void 스토리_이미지_수정() {
        // given
        Member author = MemberFixture.createDefault();
        Book book = BookFixture.createDefault();
        Story Story = FeedFixture.createStory(author, book);

        // when
        Story.changeImageKey("변경할 이미지");

        // then
        assertThat(Story.getImageKey()).isEqualTo("변경할 이미지");
    }

    @Test
    void 피드의_책_수정_성공() {
        // given
        Member author = MemberFixture.createDefault();
        Book book = BookFixture.createDefault();
        Story story = FeedFixture.createStory(author, book);
        Book newBook = BookFixture.createUpdateBook();

        // when
        story.changeBook(newBook);

        // then
        assertThat(story.getBook().getIsbn()).isEqualTo(newBook.getIsbn());
    }

    @Test
    void 스토리의_책_삭제() {
        // given
        Member author = MemberFixture.createDefault();
        Book book = BookFixture.createDefault();
        Story story = FeedFixture.createStory(author, book);

        // when
        story.changeBook(null);

        // then
        assertThat(story.getBook()).isNull();
    }
}
