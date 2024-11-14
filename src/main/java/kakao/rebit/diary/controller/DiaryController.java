package kakao.rebit.diary.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import kakao.rebit.diary.dto.DiaryRequest;
import kakao.rebit.diary.dto.DiaryResponse;
import kakao.rebit.diary.service.DiaryService;
import kakao.rebit.member.annotation.MemberInfo;
import kakao.rebit.member.dto.MemberResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/diaries")
@Tag(name = "독서일기 API", description = "독서일기 관련 API")
public class DiaryController {

    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @Operation(summary = "독서일기 목록 조회", description = "사용자의 모든 독서일기를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<DiaryResponse>> getDiaries(
            @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
            @RequestParam("date") LocalDate date
    ) {
        List<DiaryResponse> diaries = diaryService.getDiaries(memberResponse.id(), date);
        return ResponseEntity.ok(diaries);
    }

    @Operation(summary = "특정 독서일기 조회", description = "특정 ID의 독서일기를 조회합니다.")
    @GetMapping("/{diaryId}")
    public ResponseEntity<DiaryResponse> getDiaryById(
            @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
            @PathVariable("diaryId") Long diaryId) {
        DiaryResponse diary = diaryService.getDiaryById(memberResponse.id(), diaryId);
        return ResponseEntity.ok(diary);
    }

    @Operation(summary = "독서일기 작성", description = "새로운 독서일기를 작성합니다.")
    @PostMapping
    public ResponseEntity<Void> createDiary(
            @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
            @Valid @RequestBody DiaryRequest diaryRequest) {
        Long diaryId = diaryService.createDiary(memberResponse.id(), diaryRequest);
        return ResponseEntity.created(URI.create("/api/diaries/" + diaryId)).build();
    }

    @Operation(summary = "독서일기 수정", description = "특정 ID의 독서일기를 수정합니다.")
    @PutMapping("/{diaryId}")
    public ResponseEntity<Void> updateDiary(
            @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
            @PathVariable("diaryId") Long diaryId,
            @Valid @RequestBody DiaryRequest diaryRequest) {
        diaryService.updateDiary(memberResponse.id(), diaryId, diaryRequest);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "독서일기 삭제", description = "특정 ID의 독서일기를 삭제합니다.")
    @DeleteMapping("/{diaryId}")
    public ResponseEntity<Void> deleteDiary(
            @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
            @PathVariable("diaryId") Long diaryId) {
        diaryService.deleteDiary(memberResponse.id(), diaryId);
        return ResponseEntity.noContent().build();
    }
}
