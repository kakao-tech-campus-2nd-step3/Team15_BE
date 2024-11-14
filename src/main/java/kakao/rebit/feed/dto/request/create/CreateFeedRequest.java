package kakao.rebit.feed.dto.request.create;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateFavoriteBookRequest.class, name = "FB"),
        @JsonSubTypes.Type(value = CreateMagazineRequest.class, name = "M"),
        @JsonSubTypes.Type(value = CreateStoryRequest.class, name = "S")
})
public abstract class CreateFeedRequest {

    private Long bookId;

    private String type;

    protected CreateFeedRequest() {
    }

    public CreateFeedRequest(String type, Long bookId) {
        this.type = type;
        this.bookId = bookId;
    }

    public Long getBookId() {
        return bookId;
    }

    public String getType() {
        return type;
    }
}
