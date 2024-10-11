package kakao.rebit.diary.service;

import kakao.rebit.book.entity.Book;
import kakao.rebit.book.repository.BookRepository;
import kakao.rebit.diary.entity.Diary;
import kakao.rebit.diary.repository.DiaryRepository;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;

    public DiaryService(DiaryRepository diaryRepository, MemberRepository memberRepository,
        BookRepository bookRepository) {
        this.diaryRepository = diaryRepository;
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public Page<Diary> getDiaries(Long memberId, Pageable pageable) {
        return diaryRepository.findByMemberId(memberId, pageable);
    }

    @Transactional(readOnly = true)
    public Diary getDiaryById(Long memberId, Long id) {
        return diaryRepository.findById(id)
            .filter(diary -> diary.getMember().getId().equals(memberId))
            .orElseThrow(() -> new IllegalArgumentException(
                "회원 ID " + memberId + "에 해당하는 다이어리 ID " + id + "를 찾을 수 없습니다."));
    }

    @Transactional
    public Long createDiary(Long memberId, Diary diaryRequest) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("회원 ID " + memberId + "를 찾을 수 없습니다."));
        Book book = bookRepository.findByIsbn(diaryRequest.getBook().getIsbn())
            .orElseThrow(() -> new IllegalArgumentException(
                "ISBN " + diaryRequest.getBook().getIsbn() + "에 해당하는 책을 찾을 수 없습니다."));
        Diary diary = new Diary(diaryRequest.getContent(), member, book);
        Diary savedDiary = diaryRepository.save(diary);  // 저장된 다이어리 반환
        return savedDiary.getId();  // 다이어리의 ID 반환
    }

    @Transactional
    public Diary updateDiary(Long memberId, Long id, Diary diaryRequest) {
        Diary diary = diaryRepository.findById(id)
            .filter(d -> d.getMember().getId().equals(memberId))
            .orElseThrow(() -> new IllegalArgumentException(
                "회원 ID " + memberId + "에 해당하는 다이어리 ID " + id + "를 찾을 수 없습니다."));
        diary.updateDiary(diaryRequest.getContent(), diaryRequest.getBook());
        return diaryRepository.save(diary);
    }

    @Transactional
    public void deleteDiary(Long memberId, Long id) {
        Diary diary = diaryRepository.findById(id)
            .filter(d -> d.getMember().getId().equals(memberId))
            .orElseThrow(() -> new IllegalArgumentException(
                "회원 ID " + memberId + "에 해당하는 다이어리 ID " + id + "를 찾을 수 없습니다."));
        diaryRepository.delete(diary);
    }
}
