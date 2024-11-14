package kakao.rebit.feed.dto.request.create;

import static kakao.rebit.common.domain.ImageKeyModifier.BASE_IMAGE_KEY_FORMAT;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CreateMagazineRequest extends CreateFeedRequest {

    @NotBlank(message = "매거진 대상 이름은 필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "이미지는 필수입니다.")
    @Pattern(regexp = "^feed" + BASE_IMAGE_KEY_FORMAT, message = "피드 imageKey는 'feed/UUID/filename' 형식이어야 합니다.")
    private String imageKey;

    @NotBlank(message = "본문은 필수 입력 값입니다.")
    private String content;

    protected CreateMagazineRequest() {
    }

    public CreateMagazineRequest(String type, Long bookId, String name, String imageKey, String content) {
        super(type, bookId);
        this.name = name;
        this.imageKey = imageKey;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getImageKey() {
        return imageKey;
    }

    public String getContent() {
        return content;
    }
}
