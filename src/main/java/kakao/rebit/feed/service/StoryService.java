package kakao.rebit.feed.service;

import kakao.rebit.feed.dto.response.StoryResponse;
import kakao.rebit.feed.entity.Story;
import kakao.rebit.feed.repository.StoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StoryService {

    private final StoryRepository storyRepository;
    private final FeedService feedService;

    public StoryService(StoryRepository storyRepository, FeedService feedService) {
        this.storyRepository = storyRepository;
        this.feedService = feedService;
    }

    @Transactional(readOnly = true)
    public Page<StoryResponse> getStories(Pageable pageable) {
        return storyRepository.findAll(pageable).map(this::toStoryResponse);
    }

    @Transactional(readOnly = true)
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
                feedService.toAuthorResponse(story.getMember()),
                feedService.toBookResponse(story.getBook()),
                story.getType(),
                story.getImageUrl(),
                story.getContent()
        );
    }
}
