package kakao.rebit.feed.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kakao.rebit.book.entity.Book;
import kakao.rebit.common.domain.ImageKeyHolder;
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

    public void updateTextFields(String name, String content) {
        this.name = name;
        this.content = content;
    }

    @Override
    public void changeImageKey(String imageKey) {
        this.imageKey = imageKey;
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
