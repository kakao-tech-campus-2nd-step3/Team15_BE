package kakao.rebit.feed.fixture.values;

import java.util.UUID;

public record MagazineDefaultValues(
        String type,
        Long bookId,
        String name,
        String imageKey,
        String content
) {

    public static final MagazineDefaultValues INSTANCE = new MagazineDefaultValues(
            "M",
            1L,
            "연예인 이름",
            "feed/" + UUID.randomUUID() + "/default_image",
            "테스트용 컨텐츠"
    );
}
