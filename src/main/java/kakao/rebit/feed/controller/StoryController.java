package kakao.rebit.feed.controller;

import kakao.rebit.feed.dto.response.FeedResponse;
import kakao.rebit.feed.service.StoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feeds/stories")
public class StoryController {

    private final StoryService storyService;

    public StoryController(StoryService storyService) {
        this.storyService = storyService;
    }

    @GetMapping
    public ResponseEntity<Page<FeedResponse>> getStories(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(storyService.getStories(pageable));
    }

    @GetMapping("/{story-id}")
    public ResponseEntity<FeedResponse> getStory(@PathVariable("story-id") Long id) {
        return ResponseEntity.ok().body(storyService.getStoryById(id));
    }
}
