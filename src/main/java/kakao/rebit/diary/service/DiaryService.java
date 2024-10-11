package kakao.rebit.diary.service;

import kakao.rebit.book.entity.Book;
import kakao.rebit.book.repository.BookRepository;
import kakao.rebit.diary.dto.DiaryDto;
import kakao.rebit.diary.entity.Diary;
import kakao.rebit.diary.repository.DiaryRepository;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final MemberService memberService;
    private final BookRepository bookRepository;

    public DiaryService(DiaryRepository diaryRepository, MemberService memberService,
        BookRepository bookRepository) {
        this.diaryRepository = diaryRepository;
        this.memberService = memberService;
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public Page<DiaryDto> getDiariesDto(Long memberId, Pageable pageable) {
        return diaryRepository.findByMemberId(memberId, pageable)
            .map(diary -> new DiaryDto(diary.getId(), diary.getContent(), diary.getMember().getId(),
                diary.getBook().getIsbn()));
    }

    @Transactional(readOnly = true)
    public DiaryDto getDiaryDtoById(Long memberId, Long id) {
        Diary diary = diaryRepository.findByIdAndMemberId(id, memberId)
            .orElseThrow(() -> new IllegalArgumentException(
                "회원 ID " + memberId + "에 해당하는 다이어리 ID " + id + "를 찾을 수 없습니다."));
        return new DiaryDto(diary.getId(), diary.getContent(), diary.getMember().getId(),
            diary.getBook().getIsbn());
    }

    @Transactional
    public Long createDiaryFromDto(Long memberId, DiaryDto diaryDto) {
        Member member = memberService.findMemberByIdOrThrow(memberId);

        Book book = bookRepository.findByIsbn(diaryDto.isbn())
            .orElseThrow(() -> new IllegalArgumentException(
                "ISBN " + diaryDto.isbn() + "에 해당하는 책을 찾을 수 없습니다."));

        Diary diary = new Diary(diaryDto.content(), member, book);
        Diary savedDiary = diaryRepository.save(diary);
        return savedDiary.getId();
    }

    @Transactional
    public DiaryDto updateDiaryFromDto(Long memberId, Long id, DiaryDto diaryDto) {
        Diary diary = diaryRepository.findByIdAndMemberId(id, memberId)
            .orElseThrow(() -> new IllegalArgumentException(
                "회원 ID " + memberId + "에 해당하는 다이어리 ID " + id + "를 찾을 수 없습니다."));

        Book book = bookRepository.findByIsbn(diaryDto.isbn())
            .orElseThrow(() -> new IllegalArgumentException(
                "ISBN " + diaryDto.isbn() + "에 해당하는 책을 찾을 수 없습니다."));

        diary.updateDiary(diaryDto.content(), book);
        Diary updatedDiary = diaryRepository.save(diary);
        return new DiaryDto(updatedDiary.getId(), updatedDiary.getContent(),
            updatedDiary.getMember().getId(), updatedDiary.getBook().getIsbn());
    }

    @Transactional
    public void deleteDiary(Long memberId, Long id) {
        Diary diary = diaryRepository.findByIdAndMemberId(id, memberId)
            .orElseThrow(() -> new IllegalArgumentException(
                "회원 ID " + memberId + "에 해당하는 다이어리 ID " + id + "를 찾을 수 없습니다."));
        diaryRepository.delete(diary);
    }
}
