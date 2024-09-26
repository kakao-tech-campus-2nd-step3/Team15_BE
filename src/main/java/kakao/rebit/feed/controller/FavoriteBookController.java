package kakao.rebit.feed.controller;

import kakao.rebit.feed.dto.response.FavoriteBookResponse;
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
@RequestMapping("/api/favorites")
public class FavoriteBookController {

    private final FavoriteBookService favoriteBookService;

    public FavoriteBookController(FavoriteBookService favoriteBookService) {
        this.favoriteBookService = favoriteBookService;
    }

    @GetMapping
    public ResponseEntity<Page<FavoriteBookResponse>> getFavorites(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(favoriteBookService.getFavorites(pageable));
    }

    @GetMapping("/{favorite-id}")
    public ResponseEntity<FavoriteBookResponse> getFavorite(@PathVariable("favorite-id") Long id) {
        return ResponseEntity.ok().body(favoriteBookService.getFavoriteById(id));
    }
}
