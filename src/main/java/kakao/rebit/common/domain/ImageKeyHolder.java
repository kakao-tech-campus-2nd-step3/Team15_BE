package kakao.rebit.common.domain;

public interface ImageKeyHolder {

    String IMAGE_KEY_FORMAT = "^feed/[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}/[^/]+$";

    String getImageKey();

    default boolean isImageKeyUpdated(String imageKey) {
        return !this.getImageKey().equals(imageKey);
    }

    void changeImageKey(String imageKey);
}
