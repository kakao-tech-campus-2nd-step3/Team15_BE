package kakao.rebit.feed.fixture;

import kakao.rebit.book.entity.Book;
import kakao.rebit.feed.dto.request.create.CreateFavoriteBookRequest;
import kakao.rebit.feed.dto.request.create.CreateMagazineRequest;
import kakao.rebit.feed.dto.request.create.CreateStoryRequest;
import kakao.rebit.feed.dto.request.update.UpdateFavoriteBookRequest;
import kakao.rebit.feed.dto.request.update.UpdateMagazineRequest;
import kakao.rebit.feed.dto.request.update.UpdateStoryRequest;
import kakao.rebit.feed.entity.FavoriteBook;
import kakao.rebit.feed.entity.Magazine;
import kakao.rebit.feed.entity.Story;
import kakao.rebit.feed.fixture.values.FavoriteBookDefaultValues;
import kakao.rebit.feed.fixture.values.MagazineDefaultValues;
import kakao.rebit.feed.fixture.values.StoryDefaultValues;
import kakao.rebit.member.entity.Member;

public class FeedFixture {

    public static CreateFavoriteBookRequest createFavoriteBookRequestWithBookId(Long bookId) {
        return new CreateFavoriteBookRequest(
                FavoriteBookDefaultValues.INSTANCE.type(),
                bookId,
                "update-briefReview",
                "update-fullReview"
        );
    }

    public static CreateMagazineRequest createMagazineRequestWithBookId(Long bookId) {
        return new CreateMagazineRequest(
                MagazineDefaultValues.INSTANCE.type(),
                bookId,
                MagazineDefaultValues.INSTANCE.name(),
                MagazineDefaultValues.INSTANCE.imageKey(),
                "update-content"
        );
    }

    public static CreateStoryRequest createStoryRequestWithBookId(Long bookId) {
        return new CreateStoryRequest(
                StoryDefaultValues.INSTANCE.type(),
                bookId,
                StoryDefaultValues.INSTANCE.imageKey(),
                StoryDefaultValues.INSTANCE.content()
        );
    }

    public static UpdateFavoriteBookRequest updateFavoriteBookRequestWithBookId(Long bookId) {
        return new UpdateFavoriteBookRequest(
                bookId,
                FavoriteBookDefaultValues.INSTANCE.briefReview(),
                FavoriteBookDefaultValues.INSTANCE.fullReview()
        );
    }

    public static UpdateMagazineRequest updateMagazineRequestWithBookId(Long bookId) {
        return new UpdateMagazineRequest(
                bookId,
                MagazineDefaultValues.INSTANCE.imageKey(),
                MagazineDefaultValues.INSTANCE.name(),
                MagazineDefaultValues.INSTANCE.content()
        );
    }

    public static UpdateStoryRequest updateStoryRequestWithBookId(Long bookId) {
        return new UpdateStoryRequest(
                bookId,
                StoryDefaultValues.INSTANCE.imageKey(),
                "update-content"
        );
    }

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
