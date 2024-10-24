package kakao.rebit.diary.service;

import kakao.rebit.book.entity.Book;
import kakao.rebit.book.repository.BookRepository;
import kakao.rebit.diary.dto.DiaryRequest;
import kakao.rebit.diary.dto.DiaryResponse;
import kakao.rebit.diary.entity.Diary;
import kakao.rebit.diary.repository.DiaryRepository;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class DiaryService {

    private static final String DIARY_NOT_FOUND_MESSAGE = "회원 ID %d 에 해당하는 다이어리 ID %d 를 찾을 수 없습니다.";

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
    public Page<DiaryResponse> getDiaries(Long memberId, Pageable pageable) {
        return diaryRepository.findByMemberId(memberId, pageable)
            .map(diary -> new DiaryResponse(diary.getId(), diary.getContent(),
                diary.getMember().getId(),
                diary.getBook().getIsbn()));
    }

    @Transactional(readOnly = true)
    public DiaryResponse getDiaryById(Long memberId, Long id) {
        Diary diary = diaryRepository.findByIdAndMemberId(id, memberId).orElseThrow(() ->
            new IllegalArgumentException(String.format(DIARY_NOT_FOUND_MESSAGE, memberId, id)));
        return new DiaryResponse(diary.getId(), diary.getContent(), diary.getMember().getId(),
            diary.getBook().getIsbn());
    }

    @Transactional
    public Long createDiary(Long memberId, DiaryRequest diaryRequest) {
        validateDiaryRequest(diaryRequest);

        Member member = memberService.findMemberByIdOrThrow(memberId);

        Book book = bookRepository.findByIsbn(diaryRequest.isbn()).orElseThrow(() ->
            new IllegalArgumentException(String.format("ISBN %s 에 해당하는 책을 찾을 수 없습니다.", diaryRequest.isbn())));

        Diary diary = new Diary(diaryRequest.content(), member, book);
        Diary savedDiary = diaryRepository.save(diary);
        return savedDiary.getId();
    }

    @Transactional
    public void updateDiary(Long memberId, Long id, DiaryRequest diaryRequest) {
        validateDiaryRequest(diaryRequest);

        Diary diary = diaryRepository.findByIdAndMemberId(id, memberId).orElseThrow(() ->
            new IllegalArgumentException(String.format(DIARY_NOT_FOUND_MESSAGE, memberId, id)));

        Book book = bookRepository.findByIsbn(diaryRequest.isbn()).orElseThrow(() ->
            new IllegalArgumentException(String.format("ISBN %s 에 해당하는 책을 찾을 수 없습니다.", diaryRequest.isbn())));

        diary.updateDiary(diaryRequest.content(), book);
        diaryRepository.save(diary);
    }

    @Transactional
    public void deleteDiary(Long memberId, Long id) {
        Diary diary = diaryRepository.findByIdAndMemberId(id, memberId).orElseThrow(() ->
            new IllegalArgumentException(String.format(DIARY_NOT_FOUND_MESSAGE, memberId, id)));
        diaryRepository.delete(diary);
    }

    private void validateDiaryRequest(DiaryRequest diaryRequest) {
        if (!StringUtils.hasText(diaryRequest.isbn())) {
            throw new IllegalArgumentException("ISBN 값은 필수입니다.");
        }
        if (!StringUtils.hasText(diaryRequest.content())) {
            throw new IllegalArgumentException("일기 내용은 필수입니다.");
        }
    }
}
