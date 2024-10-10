package kakao.rebit.diary.controller;

import kakao.rebit.diary.entity.Diary;
import kakao.rebit.diary.service.DiaryService;
import kakao.rebit.member.annotation.MemberInfo;
import kakao.rebit.member.dto.MemberResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    // 독서일기 조회
    @GetMapping
    public ResponseEntity<List<Diary>> getDiaries(@MemberInfo MemberResponse memberResponse) {
        List<Diary> diaries = diaryService.getDiaries(memberResponse.id());
        return ResponseEntity.ok(diaries);
    }

    // 특정 독서일기 조회
    @GetMapping("/{id}")
    public ResponseEntity<Diary> getDiaryById(@MemberInfo MemberResponse memberResponse, @PathVariable Long id) {
        Diary diary = diaryService.getDiaryById(memberResponse.id(), id);
        return ResponseEntity.ok(diary);
    }

    // 독서일기 작성
    @PostMapping
    public ResponseEntity<Void> createDiary(@MemberInfo MemberResponse memberResponse, @RequestBody Diary diaryRequest) {
        diaryService.createDiary(memberResponse.id(), diaryRequest);
        return ResponseEntity.noContent().build();
    }

    // 독서일기 수정
    @PutMapping("/{id}")
    public ResponseEntity<Diary> updateDiary(@MemberInfo MemberResponse memberResponse, @PathVariable Long id, @RequestBody Diary diaryRequest) {
        Diary updatedDiary = diaryService.updateDiary(memberResponse.id(), id, diaryRequest);
        return ResponseEntity.ok(updatedDiary);
    }

    // 독서일기 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiary(@MemberInfo MemberResponse memberResponse, @PathVariable Long id) {
        diaryService.deleteDiary(memberResponse.id(), id);
        return ResponseEntity.noContent().build();
    }
}
