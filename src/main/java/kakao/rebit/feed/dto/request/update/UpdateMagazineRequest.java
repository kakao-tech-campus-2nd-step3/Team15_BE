package kakao.rebit.feed.dto.request.update;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public class UpdateMagazineRequest extends UpdateFeedRequest {

    @NotBlank(message = "메거진 대상 이름은 필수 입력 값입니다.")
    private String name;
    @NotBlank(message = "이미지는 필수 입니다.")
    private String imageKey;
    @NotBlank(message = "본문은 필수 입력 값입니다.")
    private String content;

    private UpdateMagazineRequest() {
    }

    public UpdateMagazineRequest(Long bookId, String name, String imageKey, String content) {
        super(bookId);
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
