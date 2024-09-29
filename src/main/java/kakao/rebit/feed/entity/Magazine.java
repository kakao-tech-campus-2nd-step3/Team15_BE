package kakao.rebit.feed.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kakao.rebit.book.entity.Book;
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
    public void updateNonNullFields(Feed feed) {
        if (feed.getBook() != null) {
            super.updateNonNullFields(feed);
        }
        if (((Magazine) feed).getName() != null) {
            this.name = ((Magazine) feed).getName();
        }
        if (((Magazine) feed).getImageUrl() != null) {
            this.imageUrl = ((Magazine) feed).getImageUrl();
        }
        if (((Magazine) feed).getContent() != null) {
            this.content = ((Magazine) feed).getContent();
        }
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
