package kakao.rebit.feed.fixture;

import kakao.rebit.book.entity.Book;
import kakao.rebit.feed.entity.FavoriteBook;
import kakao.rebit.feed.entity.Magazine;
import kakao.rebit.feed.entity.Story;
import kakao.rebit.feed.fixture.values.FavoriteBookDefaultValues;
import kakao.rebit.feed.fixture.values.MagazineDefaultValues;
import kakao.rebit.feed.fixture.values.StoryDefaultValues;
import kakao.rebit.member.entity.Member;

public class FeedFixture {

    public static FavoriteBook createFavoriteBook(Member member, Book book) {
        FavoriteBookDefaultValues defaults = FavoriteBookDefaultValues.INSTANCE;
        return new FavoriteBook(
                member,
                book,
                defaults.briefReview(),
                defaults.fullReview()
        );
    }

    public static Magazine createMagazine(Member member, Book book) {
        MagazineDefaultValues defaults = MagazineDefaultValues.INSTANCE;
        return new Magazine(
                member,
                book,
                defaults.name(),
                defaults.imageKey(),
                defaults.content()
        );
    }

    public static Story createStory(Member member, Book book) {
        StoryDefaultValues defaults = StoryDefaultValues.INSTANCE;
        return new Story(
                member,
                book,
                defaults.imageKey(),
                defaults.content()
        );
    }
}
