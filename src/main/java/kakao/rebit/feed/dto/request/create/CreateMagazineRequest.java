package kakao.rebit.feed.dto.request.create;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public class CreateMagazineRequest extends CreateFeedRequest {

    @NotBlank(message = "메거진 대상 이름은 필수 입력 값입니다.")
    private String name;
    @URL(message = "잘못된 URL 형식입니다.")
    private String imageUrl;
    @NotBlank(message = "본문은 필수 입력 값입니다.")
    private String content;

    private CreateMagazineRequest() {
    }

    public CreateMagazineRequest(Long bookId, String name, String imageUrl, String content) {
        super(bookId);
        this.name = name;
        this.imageUrl = imageUrl;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getContent() {
        return content;
    }
}
