package kakao.rebit.feed.fixture.values;

public record MagazineDefaultValues(
        String name,
        String imageKey,
        String content
) {

    public static final MagazineDefaultValues INSTANCE = new MagazineDefaultValues(
            "연예인 이름",
            "default-image-key",
            "테스트용 컨텐츠"
    );
}
