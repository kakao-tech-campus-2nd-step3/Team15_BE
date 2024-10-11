package kakao.rebit.feed.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import kakao.rebit.book.entity.Book;
import kakao.rebit.common.domain.ImageKeyHolder;
import kakao.rebit.feed.dto.request.update.UpdateFeedRequest;
import kakao.rebit.feed.dto.request.update.UpdateStoryRequest;
import kakao.rebit.member.entity.Member;

@Entity
@DiscriminatorValue("S")
public class Story extends Feed implements ImageKeyHolder {

    private String imageKey;

    private String content;

    protected Story() {
    }

    public Story(Member member, Book book, String imageKey, String content) {
        super(member, book);
        this.imageKey = imageKey;
        this.content = content;
    }

    @Override
    public void updateAllExceptBook(UpdateFeedRequest feedRequest) {
        this.imageKey = ((UpdateStoryRequest) feedRequest).getImageKey();
        this.content = ((UpdateStoryRequest) feedRequest).getContent();
    }

    @Override
    public String getImageKey() {
        return imageKey;
    }

    public String getContent() {
        return content;
    }
}
