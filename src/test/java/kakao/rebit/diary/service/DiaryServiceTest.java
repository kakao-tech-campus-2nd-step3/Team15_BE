package kakao.rebit.diary.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
        diaryRequest = new DiaryRequest("기본 내용", book.getIsbn(), LocalDate.of(2024, 12, 10));
    }

    @Test
    void 독서일기_목록_조회_성공() {
        // given
        LocalDate date = LocalDate.of(2024, 12, 10);
        int year = date.getYear();
        int month = date.getMonthValue();
        when(diaryRepository.findByMemberIdAndYearAndMonth(member.getId(), year, month))
                .thenReturn(List.of(diary));

        // when
        List<?> result = diaryService.getDiaries(member.getId(), date);

        // then
        assertThat(result).hasSize(1);
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
