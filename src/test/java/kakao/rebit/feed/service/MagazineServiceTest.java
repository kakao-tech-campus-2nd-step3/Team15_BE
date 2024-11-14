package kakao.rebit.feed.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import kakao.rebit.book.entity.Book;
import kakao.rebit.book.service.BookService;
import kakao.rebit.feed.dto.request.update.UpdateMagazineRequest;
import kakao.rebit.feed.entity.Magazine;
import kakao.rebit.feed.repository.MagazineRepository;
import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("매거진 서비스 테스트")
class MagazineServiceTest {

    @InjectMocks
    MagazineService magazineService;

    @Mock
    MagazineRepository magazineRepository;

    @Mock
    BookService bookService;

    @Mock
    MemberService memberService;


    @Test
    void 매거진_수정_테스트() {
        //given
        MemberResponse memberResponse = mock(MemberResponse.class);
        given(memberResponse.id()).willReturn(1L);

        Member author = mock(Member.class);
        given(memberService.findMemberByIdOrThrow(anyLong())).willReturn(author);

        Magazine magazine = mock(Magazine.class);
        given(magazineRepository.findById(anyLong())).willReturn(Optional.of(magazine));
        given(magazine.isWrittenBy(author)).willReturn(true);

        Book book = mock(Book.class);
        given(bookService.findBookIfBookIdExist(anyLong())).willReturn(Optional.ofNullable(book));
        doNothing().when(magazine).changeBook(book);

        given(magazine.getImageKey()).willReturn("image-key");

        UpdateMagazineRequest updateRequest = mock(UpdateMagazineRequest.class);
        given(updateRequest.imageKey()).willReturn("change-image-key");
        given(updateRequest.name()).willReturn("name");
        given(updateRequest.content()).willReturn("content");

        doNothing().when(magazine).changeImageKey(any(String.class));
        doNothing().when(magazine).updateTextFields(any(String.class), any(String.class));

        // when
        magazineService.updateMagazine(memberResponse, 1L, updateRequest);

        // then
        verify(memberService).findMemberByIdOrThrow(anyLong());
        verify(magazineRepository).findById(anyLong());
        verify(bookService).findBookIfBookIdExist(anyLong());
        verify(magazine).isWrittenBy(author);
        verify(magazine).getImageKey();
        verify(magazine).changeBook(book);
        verify(magazine).changeImageKey(any(String.class));
        verify(magazine).updateTextFields(any(String.class), any(String.class));
    }
}
