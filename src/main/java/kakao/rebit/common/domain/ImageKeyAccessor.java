package kakao.rebit.common.domain;

public interface ImageKeyAccessor {

    String BASE_IMAGE_KEY_FORMAT = "/[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}/[^/]+$";

    String getImageKey();
}
