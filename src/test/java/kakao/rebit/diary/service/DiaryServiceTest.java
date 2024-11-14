package kakao.rebit.diary.service;

import kakao.rebit.book.entity.Book;
import kakao.rebit.book.exception.book.BookNotFoundException;
import kakao.rebit.book.fixture.BookFixture;
import kakao.rebit.book.repository.BookRepository;
import kakao.rebit.diary.dto.DiaryRequest;
import kakao.rebit.diary.entity.Diary;
import kakao.rebit.diary.exception.DiaryNotFoundException;
import kakao.rebit.diary.fixture.DiaryFixture;
import kakao.rebit.diary.repository.DiaryRepository;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.fixture.MemberFixture;
import kakao.rebit.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DiaryServiceTest {

    @InjectMocks
    private DiaryService diaryService;

    @Mock
    private DiaryRepository diaryRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private BookRepository bookRepository;

    private Member member;
    private Book book;
    private Diary diary;
    private DiaryRequest diaryRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        member = MemberFixture.createDefault();
        book = BookFixture.createDefault();
        diary = DiaryFixture.createDefaultDiary(member, book);
        diaryRequest = new DiaryRequest("기본 내용", book.getIsbn(), "2024-11-13");
    }

    @Test
    void 독서일기_목록_조회_성공() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        when(diaryRepository.findByMemberId(member.getId(), pageable))
                .thenReturn(new PageImpl<>(List.of(diary)));

        // when
        Page<?> result = diaryService.getDiaries(member.getId(), pageable);

        // then
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void 특정_독서일기_조회_성공() {
        // given
        when(diaryRepository.findByIdAndMemberId(diary.getId(), member.getId()))
                .thenReturn(Optional.of(diary));

        // when
        var response = diaryService.getDiaryById(member.getId(), diary.getId());

        // then
        assertNotNull(response);
        assertEquals(diary.getContent(), response.content());
    }

    @Test
    void 특정_독서일기_조회_실패_일기없음() {
        // given
        when(diaryRepository.findByIdAndMemberId(diary.getId(), member.getId()))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(DiaryNotFoundException.class,
                () -> diaryService.getDiaryById(member.getId(), diary.getId()));
    }

    @Test
    void 독서일기_작성_성공() {
        // given
        when(memberService.findMemberByIdOrThrow(member.getId())).thenReturn(member);
        when(bookRepository.findByIsbn(diaryRequest.isbn())).thenReturn(Optional.of(book));
        when(diaryRepository.save(any(Diary.class))).thenReturn(diary);

        // when
        Long diaryId = diaryService.createDiary(member.getId(), diaryRequest);

        // then
        assertEquals(diary.getId(), diaryId);
    }

    @Test
    void 독서일기_작성_실패_책없음() {
        // given
        when(memberService.findMemberByIdOrThrow(member.getId())).thenReturn(member);
        when(bookRepository.findByIsbn(diaryRequest.isbn())).thenReturn(Optional.empty());

        // when, then
        assertThrows(BookNotFoundException.class,
                () -> diaryService.createDiary(member.getId(), diaryRequest));
    }

    @Test
    void 독서일기_수정_성공() {
        // given
        when(diaryRepository.findByIdAndMemberId(diary.getId(), member.getId()))
                .thenReturn(Optional.of(diary));
        when(bookRepository.findByIsbn(diaryRequest.isbn())).thenReturn(Optional.of(book));

        // when
        diaryService.updateDiary(member.getId(), diary.getId(), diaryRequest);

        // then
        assertEquals(diaryRequest.content(), diary.getContent());
    }

    @Test
    void 독서일기_수정_실패_일기없음() {
        // given
        when(diaryRepository.findByIdAndMemberId(diary.getId(), member.getId()))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(DiaryNotFoundException.class,
                () -> diaryService.updateDiary(member.getId(), diary.getId(), diaryRequest));
    }

    @Test
    void 독서일기_삭제_성공() {
        // given
        when(diaryRepository.findByIdAndMemberId(diary.getId(), member.getId()))
                .thenReturn(Optional.of(diary));

        // when
        diaryService.deleteDiary(member.getId(), diary.getId());

        // then
        verify(diaryRepository, times(1)).delete(diary);
    }

    @Test
    void 독서일기_삭제_실패_일기없음() {
        // given
        when(diaryRepository.findByIdAndMemberId(diary.getId(), member.getId()))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(DiaryNotFoundException.class,
                () -> diaryService.deleteDiary(member.getId(), diary.getId()));
    }
}
