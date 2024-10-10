package kakao.rebit.wishlist.controller;

import kakao.rebit.member.annotation.MemberInfo;
import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.wishlist.service.BookWishlistService;
import kakao.rebit.wishlist.service.ChallengeWishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishes")
public class WishlistController {

    private final BookWishlistService bookWishlistService;
    private final ChallengeWishlistService challengeWishlistService;

    public WishlistController(BookWishlistService bookWishlistService, ChallengeWishlistService challengeWishlistService) {
        this.bookWishlistService = bookWishlistService;
        this.challengeWishlistService = challengeWishlistService;
    }

    // 책 위시 목록 조회
    @GetMapping("/books")
    public ResponseEntity<List<String>> getBookWishlist(@MemberInfo MemberResponse memberResponse) {
        List<String> bookWishlist = bookWishlistService.getBookWishlist(memberResponse.id());
        return ResponseEntity.ok(bookWishlist);
    }


    // 책 위시 추가
    @PostMapping("/books/{isbn}")
    public ResponseEntity<Void> addBookWishlist(@MemberInfo MemberResponse memberResponse, @PathVariable String isbn) {
        bookWishlistService.addBookWishlist(memberResponse.id(), isbn);
        return ResponseEntity.noContent().build();
    }

    // 책 위시 삭제
    @DeleteMapping("/books/{isbn}")
    public ResponseEntity<Void> deleteBookWishlist(@MemberInfo MemberResponse memberResponse, @PathVariable String isbn) {
        bookWishlistService.deleteBookWishlist(memberResponse.id(), isbn);
        return ResponseEntity.noContent().build();
    }

    // 챌린지 위시 목록 조회
    @GetMapping("/challenges")
    public ResponseEntity<List<Long>> getChallengeWishlist(@MemberInfo MemberResponse memberResponse) {
        List<Long> challengeWishlist = challengeWishlistService.getChallengeWishlist(memberResponse.id());
        return ResponseEntity.ok(challengeWishlist);
    }

    // 챌린지 위시 추가
    @PostMapping("/challenges/{challengeId}")
    public ResponseEntity<Void> addChallengeWishlist(@MemberInfo MemberResponse memberResponse, @PathVariable Long challengeId) {
        challengeWishlistService.addChallengeWishlist(memberResponse.id(), challengeId);
        return ResponseEntity.noContent().build();
    }

    // 챌린지 위시 삭제
    @DeleteMapping("/challenges/{challengeId}")
    public ResponseEntity<Void> deleteChallengeWishlist(@MemberInfo MemberResponse memberResponse, @PathVariable Long challengeId) {
        challengeWishlistService.deleteChallengeWishlist(memberResponse.id(), challengeId);
        return ResponseEntity.noContent().build();
    }
}
