package kakao.rebit.feed.service;

import kakao.rebit.book.entity.Book;
import kakao.rebit.feed.dto.response.BookResponse;
import kakao.rebit.feed.dto.response.FavoriteBookResponse;
import kakao.rebit.feed.dto.response.FeedResponse;
import kakao.rebit.feed.dto.response.MagazineResponse;
import kakao.rebit.feed.dto.response.StoryResponse;
import kakao.rebit.feed.entity.FavoriteBook;
import kakao.rebit.feed.entity.Feed;
import kakao.rebit.feed.entity.Magazine;
import kakao.rebit.feed.entity.Story;
import kakao.rebit.feed.repository.FeedRepository;
import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedService {

    private final FeedRepository feedRepository;

    public FeedService(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    @Transactional(readOnly = true)
    public Page<FeedResponse> getFeeds(Pageable pageable) {
        return feedRepository.findAll(pageable).map(this::toFeedResponse);
    }

    /**
     * 아래부터는 DTO 변환 로직
     */

    private FeedResponse toFeedResponse(Feed feed) {
        if (feed instanceof FavoriteBook) {
            return new FavoriteBookResponse(feed.getId(), toMemberResponse(feed.getMember()),
                    toBookResponse(feed.getBook()), feed.getType(),
                    ((FavoriteBook) feed).getBriefReview(), ((FavoriteBook) feed).getFullReview());
        }
        if (feed instanceof Magazine) {
            return new MagazineResponse(feed.getId(), toMemberResponse(feed.getMember()),
                    toBookResponse(feed.getBook()), feed.getType(),
                    ((Magazine) feed).getName(), ((Magazine) feed).getImageUrl(),
                    ((Magazine) feed).getContent());
        }
        if (feed instanceof Story) {
            return new StoryResponse(feed.getId(), toMemberResponse(feed.getMember()),
                    toBookResponse(feed.getBook()), feed.getType(),
                    ((Story) feed).getImageUrl(), ((Story) feed).getContent());
        }
        throw new IllegalStateException("유효하지 않는 피드입니다.");
    }


    private MemberResponse toMemberResponse(Member member) {
        return new MemberResponse(member.getId(), member.getNickname(), member.getImageUrl(),
                member.getBio(), member.getRole(), member.getPoint());
    }

    private BookResponse toBookResponse(Book book) {
        return new BookResponse(book.getId(), book.getIsbn(), book.getTitle(),
                book.getDescription(), book.getAuthor(), book.getPublisher(), book.getImageUrl());
    }
}
