package kakao.rebit.feed.service;

import kakao.rebit.feed.dto.response.StoryResponse;
import kakao.rebit.feed.entity.Story;
import kakao.rebit.feed.mapper.FeedMapper;
import kakao.rebit.feed.repository.StoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StoryService {

    private final StoryRepository storyRepository;
    private final FeedMapper feedMapper;

    public StoryService(StoryRepository storyRepository, FeedMapper feedMapper) {
        this.storyRepository = storyRepository;
        this.feedMapper = feedMapper;
    }

    @Transactional(readOnly = true)
    public Page<StoryResponse> getStories(Pageable pageable) {
        return storyRepository.findAll(pageable).map(feedMapper::toStoryResponse);
    }

    @Transactional(readOnly = true)
    public StoryResponse getStoryById(Long id) {
        Story story = storyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("찾는 스토리가 없습니다."));
        return feedMapper.toStoryResponse(story);
    }
}
