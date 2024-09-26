package kakao.rebit.feed.service;

import kakao.rebit.book.entity.Book;
import kakao.rebit.feed.dto.response.BookResponse;
import kakao.rebit.feed.dto.response.StoryResponse;
import kakao.rebit.feed.entity.Story;
import kakao.rebit.feed.repository.StoryRepository;
import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class StoryService {

    private final StoryRepository storyRepository;

    public StoryService(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    public Page<StoryResponse> getStories(Pageable pageable) {
        return storyRepository.findAll(pageable).map(this::toStoryResponse);
    }

    public StoryResponse getStoryById(Long id) {
        Story story = storyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("찾는 스토리가 없습니다."));
        return toStoryResponse(story);
    }

    /**
     * 아래부터는 DTO 변환 로직
     */

    private StoryResponse toStoryResponse(Story story) {
        return new StoryResponse(
                story.getId(),
                toMemberResponse(story.getMember()),
                toBookResponse(story.getBook()),
                story.getImageUrl(),
                story.getContent()
        );
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
