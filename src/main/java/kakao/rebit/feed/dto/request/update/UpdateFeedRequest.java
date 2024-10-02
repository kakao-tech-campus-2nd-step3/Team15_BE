package kakao.rebit.feed.dto.request.update;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UpdateFavoriteBookRequest.class, name = "FB"),
        @JsonSubTypes.Type(value = UpdateMagazineRequest.class, name = "M"),
        @JsonSubTypes.Type(value = UpdateStoryRequest.class, name = "S")
})
public abstract class UpdateFeedRequest {

    private Long bookId;

    protected UpdateFeedRequest() {
    }

    public UpdateFeedRequest(Long bookId) {
        this.bookId = bookId;
    }

    public Long getBookId() {
        return bookId;
    }
}
