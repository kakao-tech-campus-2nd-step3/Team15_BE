package kakao.rebit.common.domain;

public interface ImageKeyModifier extends ImageKeyAccessor {

    default boolean isImageKeyUpdated(String imageKey) {
        return !this.getImageKey().equals(imageKey);
    }

    void changeImageKey(String imageKey);
}
