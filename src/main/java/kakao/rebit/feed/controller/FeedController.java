package kakao.rebit.feed.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import kakao.rebit.feed.dto.request.create.CreateFeedRequest;
import kakao.rebit.feed.dto.request.update.UpdateFeedRequest;
import kakao.rebit.feed.dto.response.FeedResponse;
import kakao.rebit.feed.dto.response.LikesMemberResponse;
import kakao.rebit.feed.service.FeedService;
import kakao.rebit.feed.service.LikesService;
import kakao.rebit.member.annotation.MemberInfo;
import kakao.rebit.member.dto.MemberResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feeds")
@Tag(name = "피드 API", description = "피드 관련 API")
public class FeedController {

    private final FeedService feedService;
    private final LikesService likesService;

    public FeedController(FeedService feedService, LikesService likesService) {
        this.feedService = feedService;
        this.likesService = likesService;
    }

    @Operation(summary = "피드 목록 조회", description = "피드 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<FeedResponse>> getFeeds(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(feedService.getFeeds(pageable));
    }

    @Operation(summary = "피드 조회", description = "피드를 조회합니다.")
    @GetMapping("/{feed-id}")
    public ResponseEntity<FeedResponse> getMagazine(@PathVariable("feed-id") Long feedId) {
        return ResponseEntity.ok().body(feedService.getFeedById(feedId));
    }

    @Operation(summary = "피드 생성", description = "피드를 생성합니다.")
    @PostMapping
    public ResponseEntity<Void> createFeed(
            @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
            @RequestBody CreateFeedRequest feedRequest) {
        Long feedId = feedService.createFeed(memberResponse, feedRequest);
        return ResponseEntity.created(URI.create("/feeds/" + feedId)).build();
    }

    @Operation(summary = "피드 수정", description = "피드를 수정합니다.")
    @PutMapping("/{feed-id}")
    public ResponseEntity<Void> updateFeed(
            @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
            @PathVariable("feed-id") Long feedId,
            @RequestBody UpdateFeedRequest feedRequest) {
        feedService.updateFeed(memberResponse, feedId, feedRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "피드 삭제", description = "피드를 삭제합니다.")
    @DeleteMapping("/{feed-id}")
    public ResponseEntity<Void> deleteFeed(
            @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
            @PathVariable("feed-id") Long feedId) {
        feedService.deleteFeedById(memberResponse, feedId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 좋아요
     */
    @Operation(summary = "좋아요 누른 멤버 목록 조회", description = "해당 피드에 좋아요를 누른 멤버 목록을 조회합니다.")
    @GetMapping("/{feed-id}/likes")
    public ResponseEntity<Page<LikesMemberResponse>> getLikesMembers(
            @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
            @PathVariable("feed-id") Long feedId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok()
                .body(likesService.getLikesMembers(memberResponse, feedId, pageable));
    }

    @Operation(summary = "좋아요 추가", description = "좋아요를 추가합니다.")
    @PostMapping("/{feed-id}/likes")
    public ResponseEntity<Void> creatLikes(
            @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
            @PathVariable("feed-id") Long feedId) {
        Long likesId = likesService.createLikes(memberResponse, feedId);
        String uri = String.format("/feeds/%d/likes/%d", feedId, likesId);
        return ResponseEntity.created(URI.create(uri)).build();
    }

    @Operation(summary = "좋아요 삭제", description = "좋아요를 삭제합니다.")
    @DeleteMapping("/{feed-id}/likes")
    public ResponseEntity<Void> deleteLikes(
            @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
            @PathVariable("feed-id") Long feedId) {
        likesService.deleteLikes(memberResponse, feedId);
        return ResponseEntity.noContent().build();
    }
}
