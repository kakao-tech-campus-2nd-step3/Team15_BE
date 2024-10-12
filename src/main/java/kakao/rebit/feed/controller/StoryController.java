package kakao.rebit.feed.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kakao.rebit.common.annotation.AllowAnonymous;
import kakao.rebit.feed.dto.request.update.UpdateStoryRequest;
import kakao.rebit.feed.dto.response.FeedResponse;
import kakao.rebit.feed.dto.response.StoryResponse;
import kakao.rebit.feed.service.StoryService;
import kakao.rebit.member.annotation.MemberInfo;
import kakao.rebit.member.dto.MemberResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feeds/stories")
@Tag(name = "스토리 피드 API", description = "스토리 피드 관련 API")
public class StoryController {

    private final StoryService storyService;

    public StoryController(StoryService storyService) {
        this.storyService = storyService;
    }

    @Operation(summary = "스토리 목록 조회", description = "스토리 목록을 조회합니다.")
    @AllowAnonymous
    @GetMapping
    public ResponseEntity<Page<FeedResponse>> getStories(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(storyService.getStories(pageable));
    }

    @Operation(summary = "스토리 조회", description = "스토리를 조회합니다.")
    @ApiResponse(content = @Content(schema = @Schema(implementation = StoryResponse.class)))
    @GetMapping("/{story-id}")
    public ResponseEntity<FeedResponse> getStory(@PathVariable("story-id") Long storyId) {
        return ResponseEntity.ok().body(storyService.getStoryById(storyId));
    }

    @Operation(summary = "스토리 수정", description = "스토리를 수정합니다.")
    @PutMapping("/{story-id}")
    public ResponseEntity<Void> updateStory(
            @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
            @PathVariable("story-id") Long storyId,
            @Valid @RequestBody UpdateStoryRequest updateStoryRequest) {
        storyService.updateStory(memberResponse, storyId, updateStoryRequest);
        return ResponseEntity.ok().build();
    }
}
