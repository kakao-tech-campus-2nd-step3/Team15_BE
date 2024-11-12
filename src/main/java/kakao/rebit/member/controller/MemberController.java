package kakao.rebit.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import kakao.rebit.challenge.dto.ChallengeResponse;
import kakao.rebit.challenge.service.ChallengeParticipationService;
import kakao.rebit.feed.dto.response.FavoriteBookResponse;
import kakao.rebit.feed.dto.response.FeedResponse;
import kakao.rebit.feed.dto.response.MagazineResponse;
import kakao.rebit.feed.dto.response.StoryResponse;
import kakao.rebit.feed.service.FavoriteBookService;
import kakao.rebit.feed.service.FeedService;
import kakao.rebit.feed.service.MagazineService;
import kakao.rebit.feed.service.StoryService;
import kakao.rebit.member.annotation.MemberInfo;
import kakao.rebit.member.dto.ChargePointRequest;
import kakao.rebit.member.dto.MemberProfileResponse;
import kakao.rebit.member.dto.MemberRequest;
import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.member.entity.Role;
import kakao.rebit.member.service.MemberService;
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
@RequestMapping("/api/members")
@Tag(name = "회원 API", description = "회원 관련 API")
public class MemberController {

    private final MemberService memberService;
    private final FeedService feedService;
    private final FavoriteBookService favoriteBookService;
    private final MagazineService magazineService;
    private final StoryService storyService;
    private final ChallengeParticipationService challengeParticipationService;

    public MemberController(MemberService memberService, FeedService feedService, FavoriteBookService favoriteBookService,
            MagazineService magazineService, StoryService storyService, ChallengeParticipationService challengeParticipationService) {
        this.memberService = memberService;
        this.feedService = feedService;
        this.favoriteBookService = favoriteBookService;
        this.magazineService = magazineService;
        this.storyService = storyService;
        this.challengeParticipationService = challengeParticipationService;
    }

    @Operation(summary = "포인트 조회", description = "사용자의 포인트를 조회합니다.")
    @GetMapping("/points")
    public ResponseEntity<Integer> getMyPoints(
            @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse) {
        Integer points = memberService.getPoints(memberResponse.email());
        return ResponseEntity.ok().body(points);
    }

    @Operation(summary = "포인트 충전", description = "사용자의 포인트를 충전합니다.")
    @PostMapping("/points")
    public ResponseEntity<Void> chargePoints(
            @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
            @RequestBody ChargePointRequest request) {
        memberService.chargePoints(memberResponse.email(), request.points());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "내 정보 조회", description = "사용자 자신의 정보를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<MemberProfileResponse> getMyInfo(
            @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse) {
        MemberProfileResponse response = memberService.getMemberResponseByEmail(memberResponse.email());
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "내 정보 수정", description = "사용자 자신의 정보를 수정합니다.")
    @PutMapping("/me")
    public ResponseEntity<Void> updateMyInfo(
            @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
            @Valid @RequestBody MemberRequest memberRequest) {
        memberService.updateMyMember(memberResponse.email(), memberRequest);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "모든 사용자 조회", description = "관리자 및 에디터가 모든 사용자를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<MemberProfileResponse>> getAllMembers(
            @Parameter(hidden = true) @MemberInfo(allowedRoles = {Role.ROLE_ADMIN, Role.ROLE_EDITOR}) MemberResponse memberResponse) {
        List<MemberProfileResponse> members = memberService.getAllMemberResponses();
        return ResponseEntity.ok().body(members);
    }

    @Operation(summary = "특정 사용자 조회", description = "관리자 및 에디터가 특정 사용자를 조회합니다.")
    @GetMapping("/{member-id}")
    public ResponseEntity<MemberProfileResponse> getMemberById(
            @Parameter(hidden = true) @MemberInfo(allowedRoles = {Role.ROLE_ADMIN, Role.ROLE_EDITOR}) MemberResponse memberResponse,
            @PathVariable("member-id") Long memberId) {
        MemberProfileResponse response = memberService.getMemberResponseById(memberId);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "특정 사용자 수정", description = "관리자 및 에디터가 특정 사용자의 정보를 수정합니다.")
    @PutMapping("/{member-id}")
    public ResponseEntity<MemberProfileResponse> updateMember(
            @Parameter(hidden = true) @MemberInfo(allowedRoles = {Role.ROLE_ADMIN, Role.ROLE_EDITOR}) MemberResponse memberResponse,
            @PathVariable("member-id") Long memberId,
            @Valid @RequestBody MemberRequest memberRequest) {
        memberService.updateMember(memberId, memberRequest);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "특정 사용자 삭제", description = "관리자 및 에디터가 특정 사용자를 삭제합니다.")
    @DeleteMapping("/{member-id}")
    public ResponseEntity<Void> deleteMember(
            @Parameter(hidden = true) @MemberInfo(allowedRoles = {Role.ROLE_ADMIN, Role.ROLE_EDITOR}) MemberResponse memberResponse,
            @PathVariable("member-id") Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "내 피드 목록 조회", description = "본인이 작성한 피드 목록을 조회합니다.")
    @ApiResponse(content = @Content(schema = @Schema(oneOf = {FavoriteBookResponse.class, MagazineResponse.class, StoryResponse.class})))
    @GetMapping("/feeds")
    public ResponseEntity<Page<FeedResponse>> getMyFeeds(
            @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(feedService.getMyFeeds(memberResponse, pageable));
    }

    @Operation(summary = "내 인생책 목록 조회", description = "본인이 작성한 인생책 목록을 조회합니다.")
    @GetMapping("/feeds/favorite-books")
    public ResponseEntity<Page<FavoriteBookResponse>> getMyFavoriteBooks(
            @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(favoriteBookService.getMyFavoriteBooks(memberResponse, pageable));
    }

    @Operation(summary = "내 메거진 목록 조회", description = "본인이 작성한 메거진 목록을 조회합니다.")
    @GetMapping("/feeds/magazines")
    public ResponseEntity<Page<MagazineResponse>> getMyMagazines(
            @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(magazineService.getMyMagazines(memberResponse, pageable));
    }

    @Operation(summary = "내 인생책 목록 조회", description = "본인이 작성한 인생책 목록을 조회합니다.")
    @GetMapping("/feeds/stories")
    public ResponseEntity<Page<StoryResponse>> getMyStories(
            @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(storyService.getMyStories(memberResponse, pageable));
    }

    @Operation(summary = "내 챌린지 목록 조회", description = "본인이 참여한 챌린지 목록을 조회합니다.")
    @GetMapping("/challenges")
    public ResponseEntity<Page<ChallengeResponse>> getMyChallenges(
            @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(challengeParticipationService.getMyChallenges(memberResponse, pageable));
    }
}
