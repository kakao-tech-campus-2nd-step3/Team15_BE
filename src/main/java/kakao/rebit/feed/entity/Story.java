package kakao.rebit.feed.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import kakao.rebit.book.entity.Book;
import kakao.rebit.member.entity.Member;

@Entity
@DiscriminatorValue("S")
public class Story extends Feed {

    private String imageUrl;

    private String content;

    protected Story() {
    }

    public Story(Member member, Book book, String imageUrl, String content) {
        super(member, book);
        this.imageUrl = imageUrl;
        this.content = content;
    }

    @Override
    public void updateNonNullFields(Feed feed) {
        if (feed.getBook() != null) {
            super.updateNonNullFields(feed);
        }
        if (((Story) feed).getImageUrl() != null) {
            this.imageUrl = ((Story) feed).getImageUrl();
        }
        if (((Story) feed).getContent() != null) {
            this.content = ((Story) feed).getContent();
        }
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getContent() {
        return content;
    }
}
