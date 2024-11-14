package kakao.rebit.feed.fixture.values;

import java.util.UUID;

public record MagazineDefaultValues(
        String type,
        String name,
        String imageKey,
        String content
) {

    public static final MagazineDefaultValues INSTANCE = new MagazineDefaultValues(
            "M",
            "연예인 이름",
            "feed/" + UUID.randomUUID() + "/default_image",
            "테스트용 컨텐츠"
    );
}
