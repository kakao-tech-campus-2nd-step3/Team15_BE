package kakao.rebit.feed.dto.response;

import kakao.rebit.member.dto.MemberResponse;

public class StoryResponse extends FeedResponse {

    private String imageUrl;
    private String content;

    public StoryResponse(Long id, MemberResponse memberResponse, BookResponse bookResponse,
            String type, String imageUrl, String content) {
        super(id, memberResponse, bookResponse, type);
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
