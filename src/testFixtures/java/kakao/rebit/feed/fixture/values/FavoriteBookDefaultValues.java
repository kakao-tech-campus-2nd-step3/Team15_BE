package kakao.rebit.feed.fixture.values;

public record FavoriteBookDefaultValues(
        String type,
        String briefReview,
        String fullReview
) {

    public static final FavoriteBookDefaultValues INSTANCE = new FavoriteBookDefaultValues(
            "FB",
            "테스트 용 한줄평",
            "테스트 용 서평"
    );
}
