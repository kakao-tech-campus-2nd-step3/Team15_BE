package kakao.rebit.feed.fixture.values;

public record StoryDefaultValues(
        String imageKey,
        String content
) {

    public static final StoryDefaultValues INSTANCE = new StoryDefaultValues(

            "test-for-imageKey",
            "테스트용 컨텐츠"
    );
}
