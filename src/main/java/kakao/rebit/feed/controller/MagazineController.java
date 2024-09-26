package kakao.rebit.feed.controller;

import kakao.rebit.feed.dto.response.MagazineResponse;
import kakao.rebit.feed.service.MagazineService;
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
@RequestMapping("/api/magazines")
public class MagazineController {

    private final MagazineService magazineService;

    public MagazineController(MagazineService magazineService) {
        this.magazineService = magazineService;
    }

    @GetMapping
    public ResponseEntity<Page<MagazineResponse>> getMagazines(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(magazineService.getMagazines(pageable));
    }

    @GetMapping("/{magazine-id}")
    public ResponseEntity<MagazineResponse> getMagazine(@PathVariable("magazine-id") Long id) {
        return ResponseEntity.ok().body(magazineService.getMagazineById(id));
    }
}
