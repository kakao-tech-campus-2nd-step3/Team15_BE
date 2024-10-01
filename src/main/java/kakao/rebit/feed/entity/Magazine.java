package kakao.rebit.feed.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kakao.rebit.book.entity.Book;
import kakao.rebit.feed.dto.request.update.UpdateFeedRequest;
import kakao.rebit.feed.dto.request.update.UpdateMagazineRequest;
import kakao.rebit.member.entity.Member;

@Entity
@Table(name = "magazine")
@DiscriminatorValue("M")
public class Magazine extends Feed {

    private String name;
    private String imageUrl;
    private String content;

    protected Magazine() {
    }

    public Magazine(Member member, Book book, String name, String imageUrl, String content) {
        super(member, book);
        this.name = name;
        this.imageUrl = imageUrl;
        this.content = content;
    }

    @Override
    public void updateAllExceptBook(UpdateFeedRequest feedRequest) {
        this.name = ((UpdateMagazineRequest) feedRequest).getName();
        this.imageUrl = ((UpdateMagazineRequest) feedRequest).getImageUrl();
        this.content = ((UpdateMagazineRequest) feedRequest).getContent();
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
