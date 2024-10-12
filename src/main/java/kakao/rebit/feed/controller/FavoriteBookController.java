package kakao.rebit.feed.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kakao.rebit.common.annotation.AllowAnonymous;
import kakao.rebit.feed.dto.request.update.UpdateFavoriteBookRequest;
import kakao.rebit.feed.dto.response.FavoriteBookResponse;
import kakao.rebit.feed.dto.response.FeedResponse;
import kakao.rebit.feed.service.FavoriteBookService;
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
@RequestMapping("/api/feeds/favorite-books")
@Tag(name = "인생책 피드 API", description = "인생책 피드 관련 API")
public class FavoriteBookController {

    private final FavoriteBookService favoriteBookService;

    public FavoriteBookController(FavoriteBookService favoriteBookService) {
        this.favoriteBookService = favoriteBookService;
    }

    @Operation(summary = "인생책 목록 조회", description = "인생책 목록을 조회합니다.")
    @AllowAnonymous
    @GetMapping
    public ResponseEntity<Page<FeedResponse>> getFavoriteBooks(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(favoriteBookService.getFavoriteBooks(pageable));
    }

    @Operation(summary = "인생책 조회", description = "인생책을 조회합니다.")
    @ApiResponse(content = @Content(schema = @Schema(implementation = FavoriteBookResponse.class)))
    @GetMapping("/{favorite-id}")
    public ResponseEntity<FeedResponse> getFavoriteBook(
            @PathVariable("favorite-id") Long favoriteId) {
        return ResponseEntity.ok().body(favoriteBookService.getFavoriteBookById(favoriteId));
    }

    @Operation(summary = "인생책 수정", description = "인생책을 수정합니다.")
    @PutMapping("/{favorite-id}")
    public ResponseEntity<Void> updateMagazine(
            @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
            @PathVariable("favorite-id") Long favoriteBookId,
            @Valid @RequestBody UpdateFavoriteBookRequest updateFavoriteBookRequest) {
        favoriteBookService.updateFavoriteBook(memberResponse, favoriteBookId,
                updateFavoriteBookRequest);
        return ResponseEntity.noContent().build();
    }
}
