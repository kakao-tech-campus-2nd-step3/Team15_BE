package kakao.rebit.feed.dto.response;

import kakao.rebit.member.dto.MemberResponse;

public class MagazineResponse extends FeedResponse {

    private String name;
    private String imageUrl;
    private String content;

    public MagazineResponse(Long id, MemberResponse memberResponse, BookResponse bookResponse,
            String type, String name, String imageUrl, String content) {
        super(id, memberResponse, bookResponse, type);
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
