package kakao.rebit.feed.controller;

import java.net.URI;
import kakao.rebit.feed.dto.request.create.CreateFeedRequest;
import kakao.rebit.feed.dto.request.update.UpdateFeedRequest;
import kakao.rebit.feed.dto.response.FeedResponse;
import kakao.rebit.feed.service.FeedService;
import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.member.entity.Role;
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

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
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
    public ResponseEntity<Void> createFeed(@RequestBody CreateFeedRequest feedRequest) {
        MemberResponse memberResponse = new MemberResponse(1L, "testUser", "imageUrl", "bio",
                Role.ROLE_USER, 10000);
        Long feedId = feedService.createFeed(memberResponse, feedRequest);
        return ResponseEntity.created(URI.create("/api/feeds/" + feedId)).build();
    }

    @PutMapping("/{feed-id}")
    public ResponseEntity<Void> updateFeed(@PathVariable("feed-id") Long feedId,
            @RequestBody UpdateFeedRequest feedRequest) {
        MemberResponse memberResponse = new MemberResponse(1L, "testUser", "imageUrl", "bio",
                Role.ROLE_USER, 10000);
        feedService.updateFeed(memberResponse, feedId, feedRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{feed-id}")
    public ResponseEntity<Void> deleteFeed(@PathVariable("feed-id") Long feedId) {
        feedService.deleteFeedById(feedId);
        return ResponseEntity.noContent().build();
    }
}
