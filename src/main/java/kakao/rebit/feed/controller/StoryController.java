package kakao.rebit.feed.controller;

import kakao.rebit.feed.dto.response.StoryResponse;
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
@RequestMapping("/api/stories")
public class StoryController {

    private final StoryService storyService;

    public StoryController(StoryService storyService) {
        this.storyService = storyService;
    }

    @GetMapping
    public ResponseEntity<Page<StoryResponse>> getStories(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(storyService.getStories(pageable));
    }

    @GetMapping("/{story-id}")
    public ResponseEntity<StoryResponse> getStory(@PathVariable("story-id") Long id) {
        return ResponseEntity.ok().body(storyService.getStoryById(id));
    }
}
