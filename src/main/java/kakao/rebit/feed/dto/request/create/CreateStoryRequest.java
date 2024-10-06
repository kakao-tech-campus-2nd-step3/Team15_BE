package kakao.rebit.feed.dto.request.create;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public class CreateStoryRequest extends CreateFeedRequest {

    @URL(message = "잘못된 URL 형식입니다.")
    private String imageUrl;
    @NotBlank(message = "본문은 필수 입력 값입니다.")
    private String content;

    private CreateStoryRequest() {
    }

    public CreateStoryRequest(Long bookId, String imageUrl, String content) {
        super(bookId);
        this.imageUrl = imageUrl;
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getContent() {
        return content;
    }
}
