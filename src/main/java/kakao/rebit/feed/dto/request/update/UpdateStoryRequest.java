package kakao.rebit.feed.dto.request.update;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public class UpdateStoryRequest extends UpdateFeedRequest {

    @URL(message = "잘못된 URL 형식입니다.")
    private String imageKey;
    @NotBlank(message = "본문은 필수 입력 값입니다.")
    private String content;

    private UpdateStoryRequest() {
    }

    public UpdateStoryRequest(Long bookId, String imageKey, String content) {
        super(bookId);
        this.imageKey = imageKey;
        this.content = content;
    }

    public String getImageKey() {
        return imageKey;
    }

    public String getContent() {
        return content;
    }
}
