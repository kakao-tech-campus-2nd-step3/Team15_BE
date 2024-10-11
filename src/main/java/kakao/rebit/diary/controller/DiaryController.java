package kakao.rebit.diary.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kakao.rebit.diary.entity.Diary;
import kakao.rebit.diary.service.DiaryService;
import kakao.rebit.member.annotation.MemberInfo;
import kakao.rebit.member.dto.MemberResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Page<Diary>> getDiaries(@Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
        @PageableDefault Pageable pageable) {
        Page<Diary> diaries = diaryService.getDiaries(memberResponse.id(), pageable);
        return ResponseEntity.ok(diaries);
    }

    @Operation(summary = "특정 독서일기 조회", description = "특정 ID의 독서일기를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<Diary> getDiaryById(@Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
        @PathVariable Long id) {
        Diary diary = diaryService.getDiaryById(memberResponse.id(), id);
        return ResponseEntity.ok(diary);
    }

    @Operation(summary = "독서일기 작성", description = "새로운 독서일기를 작성합니다.")
    @PostMapping
    public ResponseEntity<Void> createDiary(@Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
        @RequestBody Diary diaryRequest) {
        diaryService.createDiary(memberResponse.id(), diaryRequest);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "독서일기 수정", description = "특정 ID의 독서일기를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<Diary> updateDiary(@Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
        @PathVariable Long id, @RequestBody Diary diaryRequest) {
        Diary updatedDiary = diaryService.updateDiary(memberResponse.id(), id, diaryRequest);
        return ResponseEntity.ok(updatedDiary);
    }

    @Operation(summary = "독서일기 삭제", description = "특정 ID의 독서일기를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiary(@Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
        @PathVariable Long id) {
        diaryService.deleteDiary(memberResponse.id(), id);
        return ResponseEntity.noContent().build();
    }
}
