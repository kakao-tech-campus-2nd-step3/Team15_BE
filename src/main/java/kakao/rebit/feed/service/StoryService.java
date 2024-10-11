package kakao.rebit.feed.service;

import kakao.rebit.feed.dto.response.FeedResponse;
import kakao.rebit.feed.entity.Story;
import kakao.rebit.feed.exception.feed.FeedNotFoundException;
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
    public Page<FeedResponse> getStories(Pageable pageable) {
        return storyRepository.findAll(pageable).map(feedMapper::toFeedResponse);
    }

    @Transactional(readOnly = true)
    public FeedResponse getStoryById(Long id) {
        Story story = storyRepository.findById(id)
                .orElseThrow(() -> FeedNotFoundException.EXCEPTION);
        return feedMapper.toFeedResponse(story);
    }
}
