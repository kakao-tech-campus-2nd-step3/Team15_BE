package kakao.rebit.feed.dto.request.update;

import jakarta.validation.constraints.NotBlank;

public class UpdateStoryRequest extends UpdateFeedRequest {

    @NotBlank(message = "이미지는 필수 입니다.")
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
