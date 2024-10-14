package kakao.rebit.feed.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kakao.rebit.common.annotation.AllowAnonymous;
import kakao.rebit.feed.dto.request.update.UpdateMagazineRequest;
import kakao.rebit.feed.dto.response.MagazineResponse;
import kakao.rebit.feed.service.MagazineService;
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
@RequestMapping("/api/feeds/magazines")
@Tag(name = "메거진 피드 API", description = "메거진 피드 관련 API")
public class MagazineController {

    private final MagazineService magazineService;

    public MagazineController(MagazineService magazineService) {
        this.magazineService = magazineService;
    }

    @Operation(summary = "메거진 목록 조회", description = "메거진 목록을 조회합니다.")
    @AllowAnonymous
    @GetMapping
    public ResponseEntity<Page<MagazineResponse>> getMagazines(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(magazineService.getMagazines(pageable));
    }

    @Operation(summary = "메거진 조회", description = "메거진을 조회합니다.")
    @GetMapping("/{magazine-id}")
    public ResponseEntity<MagazineResponse> getMagazine(
            @PathVariable("magazine-id") Long magazineId) {
        return ResponseEntity.ok().body(magazineService.getMagazineById(magazineId));
    }

    @Operation(summary = "메거진 수정", description = "메거진을 수정합니다.")
    @PutMapping("/{magazine-id}")
    public ResponseEntity<Void> updateMagazine(
            @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
            @PathVariable("magazine-id") Long magazineId,
            @Valid @RequestBody UpdateMagazineRequest updateMagazineRequest) {
        magazineService.updateMagazine(memberResponse, magazineId, updateMagazineRequest);
        return ResponseEntity.noContent().build();
    }
}
