package kakao.rebit.feed.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kakao.rebit.book.entity.Book;
import kakao.rebit.common.domain.ImageKeyHolder;
import kakao.rebit.feed.dto.request.update.UpdateFeedRequest;
import kakao.rebit.feed.dto.request.update.UpdateMagazineRequest;
import kakao.rebit.member.entity.Member;

@Entity
@Table(name = "magazine")
@DiscriminatorValue("M")
public class Magazine extends Feed implements ImageKeyHolder {

    private String name;
    private String imageKey;
    private String content;

    protected Magazine() {
    }

    public Magazine(Member member, Book book, String name, String imageKey, String content) {
        super(member, book);
        this.name = name;
        this.imageKey = imageKey;
        this.content = content;
    }

    @Override
    public void updateAllExceptBook(UpdateFeedRequest feedRequest) {
        this.name = ((UpdateMagazineRequest) feedRequest).getName();
        this.imageKey = ((UpdateMagazineRequest) feedRequest).getImageKey();
        this.content = ((UpdateMagazineRequest) feedRequest).getContent();
    }

    public String getName() {
        return name;
    }

    @Override
    public String getImageKey() {
        return imageKey;
    }

    public String getContent() {
        return content;
    }
}
