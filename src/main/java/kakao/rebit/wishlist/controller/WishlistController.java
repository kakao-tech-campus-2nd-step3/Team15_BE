package kakao.rebit.wishlist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kakao.rebit.member.annotation.MemberInfo;
import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.wishlist.service.BookWishlistService;
import kakao.rebit.wishlist.service.ChallengeWishlistService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishes")
@Tag(name = "위시리스트 API", description = "책 및 챌린지 위시리스트 관련 API")
public class WishlistController {

    private final BookWishlistService bookWishlistService;
    private final ChallengeWishlistService challengeWishlistService;

    public WishlistController(BookWishlistService bookWishlistService,
        ChallengeWishlistService challengeWishlistService) {
        this.bookWishlistService = bookWishlistService;
        this.challengeWishlistService = challengeWishlistService;
    }

    @Operation(summary = "책 위시 목록 조회", description = "사용자가 찜한 책 목록을 조회합니다.")
    @GetMapping("/books")
    public ResponseEntity<Page<String>> getBookWishlist(
        @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
        @PageableDefault(size = 10) Pageable pageable) {
        Page<String> bookWishlist = bookWishlistService.getBookWishlist(memberResponse.id(),
            pageable);
        return ResponseEntity.ok(bookWishlist);
    }

    @Operation(summary = "책 위시 추가", description = "사용자가 책을 위시리스트에 추가합니다.")
    @PostMapping("/books/{isbn}")
    public ResponseEntity<Void> addBookWishlist(
        @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
        @PathVariable String isbn) {
        bookWishlistService.addBookWishlist(memberResponse.id(), isbn);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "책 위시 삭제", description = "사용자가 책을 위시리스트에서 삭제합니다.")
    @DeleteMapping("/books/{isbn}")
    public ResponseEntity<Void> deleteBookWishlist(
        @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
        @PathVariable String isbn) {
        bookWishlistService.deleteBookWishlist(memberResponse.id(), isbn);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "챌린지 위시 목록 조회", description = "사용자가 찜한 챌린지 목록을 조회합니다.")
    @GetMapping("/challenges")
    public ResponseEntity<Page<Long>> getChallengeWishlist(
        @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
        @PageableDefault(size = 10) Pageable pageable) {
        Page<Long> challengeWishlist = challengeWishlistService.getChallengeWishlist(
            memberResponse.id(), pageable);
        return ResponseEntity.ok(challengeWishlist);
    }

    @Operation(summary = "챌린지 위시 추가", description = "사용자가 챌린지를 위시리스트에 추가합니다.")
    @PostMapping("/challenges/{challengeId}")
    public ResponseEntity<Void> addChallengeWishlist(
        @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
        @PathVariable Long challengeId) {
        challengeWishlistService.addChallengeWishlist(memberResponse.id(), challengeId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "챌린지 위시 삭제", description = "사용자가 챌린지를 위시리스트에서 삭제합니다.")
    @DeleteMapping("/challenges/{challengeId}")
    public ResponseEntity<Void> deleteChallengeWishlist(
        @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
        @PathVariable Long challengeId) {
        challengeWishlistService.deleteChallengeWishlist(memberResponse.id(), challengeId);
        return ResponseEntity.noContent().build();
    }
}
