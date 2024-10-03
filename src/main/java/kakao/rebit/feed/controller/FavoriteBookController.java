package kakao.rebit.feed.controller;

import kakao.rebit.feed.dto.response.FeedResponse;
import kakao.rebit.feed.service.FavoriteBookService;
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
@RequestMapping("/api/feeds/favorite-books")
public class FavoriteBookController {

    private final FavoriteBookService favoriteBookService;

    public FavoriteBookController(FavoriteBookService favoriteBookService) {
        this.favoriteBookService = favoriteBookService;
    }

    @GetMapping
    public ResponseEntity<Page<FeedResponse>> getFavoriteBooks(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(favoriteBookService.getFavoriteBooks(pageable));
    }

    @GetMapping("/{favorite-id}")
    public ResponseEntity<FeedResponse> getFavoriteBook(
            @PathVariable("favorite-id") Long id) {
        return ResponseEntity.ok().body(favoriteBookService.getFavoriteBookById(id));
    }
}
