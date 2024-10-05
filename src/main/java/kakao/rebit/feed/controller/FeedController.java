package kakao.rebit.feed.controller;

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
public class FeedController {

    private final FeedService feedService;
    private final LikesService likesService;

    public FeedController(FeedService feedService, LikesService likesService) {
        this.feedService = feedService;
        this.likesService = likesService;
    }

    @GetMapping
    public ResponseEntity<Page<FeedResponse>> getFeeds(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(feedService.getFeeds(pageable));
    }

    @GetMapping("/{feed-id}")
    public ResponseEntity<FeedResponse> getMagazine(@PathVariable("feed-id") Long feedId) {
        return ResponseEntity.ok().body(feedService.getFeedById(feedId));
    }

    @PostMapping
    public ResponseEntity<Void> createFeed(@MemberInfo MemberResponse memberResponse, @RequestBody CreateFeedRequest feedRequest) {
        Long feedId = feedService.createFeed(memberResponse, feedRequest);
        return ResponseEntity.created(URI.create("/feeds/" + feedId)).build();
    }

    @PutMapping("/{feed-id}")
    public ResponseEntity<Void> updateFeed(@MemberInfo MemberResponse memberResponse, @PathVariable("feed-id") Long feedId,
            @RequestBody UpdateFeedRequest feedRequest) {
       feedService.updateFeed(memberResponse, feedId, feedRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{feed-id}")
    public ResponseEntity<Void> deleteFeed(@PathVariable("feed-id") Long feedId) {
        feedService.deleteFeedById(feedId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 좋아요
     */
    @GetMapping("/{feed-id}/likes")
    public ResponseEntity<Page<LikesMemberResponse>> getLikesMembers(
            @PathVariable("feed-id") Long feedId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(likesService.getLikesMembers(feedId, pageable));
    }

    @PostMapping("/{feed-id}/likes")
    public ResponseEntity<Void> creatLikes(@MemberInfo MemberResponse memberResponse, @PathVariable("feed-id") Long feedId) {
        Long likesId = likesService.createLikes(memberResponse, feedId);
        String uri = String.format("/feeds/%d/likes/%d", feedId, likesId);
        return ResponseEntity.created(URI.create(uri)).build();
    }

    @DeleteMapping("/{feed-id}/likes")
    public ResponseEntity<Void> deleteLikes(@MemberInfo MemberResponse memberResponse, @PathVariable("feed-id") Long feedId) {
        likesService.deleteLikes(memberResponse, feedId);
        return ResponseEntity.noContent().build();
    }
}
