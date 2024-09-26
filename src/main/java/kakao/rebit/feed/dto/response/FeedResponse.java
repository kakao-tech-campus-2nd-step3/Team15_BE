package kakao.rebit.feed.dto.response;

import kakao.rebit.member.dto.MemberResponse;

public abstract class FeedResponse {

    private Long id;
    private MemberResponse memberResponse;
    private BookResponse bookResponse;
    private String type;

    public FeedResponse(Long id, MemberResponse memberResponse, BookResponse bookResponse,
            String type) {
        this.id = id;
        this.memberResponse = memberResponse;
        this.bookResponse = bookResponse;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public MemberResponse getMemberResponse() {
        return memberResponse;
    }

    public BookResponse getBookResponse() {
        return bookResponse;
    }

    public String getType() {
        return type;
    }
}
