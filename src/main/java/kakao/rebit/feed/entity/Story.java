package kakao.rebit.feed.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import kakao.rebit.member.entity.Member;

@Entity
@DiscriminatorValue("S")
public class Story extends Feed {

    private String imageUrl;

    private String content;

    protected Story() {
    }

    public Story(Member member, int likes, Book book, String imageUrl, String content) {
        super(member, likes, book);
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
