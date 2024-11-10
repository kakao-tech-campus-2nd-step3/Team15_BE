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
import kakao.rebit.feed.dto.request.update.UpdateFavoriteBookRequest;
import kakao.rebit.feed.entity.FavoriteBook;
import kakao.rebit.feed.repository.FavoriteBookRepository;
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
@DisplayName("인생책 서비스 테스트")
class FavoriteBookServiceTest {

    @InjectMocks
    FavoriteBookService favoriteBookService;

    @Mock
    FavoriteBookRepository favoriteBookRepository;

    @Mock
    BookService bookService;

    @Mock
    MemberService memberService;


    @Test
    void 인생책_수정_테스트() {
        //given
        MemberResponse memberResponse = mock(MemberResponse.class);
        given(memberResponse.id()).willReturn(1L);

        Member author = mock(Member.class);
        given(memberService.findMemberByIdOrThrow(anyLong())).willReturn(author);

        FavoriteBook favoriteBook = mock(FavoriteBook.class);
        given(favoriteBookRepository.findById(anyLong())).willReturn(Optional.of(favoriteBook));
        given(favoriteBook.isWrittenBy(author)).willReturn(true);

        Book book = mock(Book.class);
        given(bookService.findBookByIdOrThrow(anyLong())).willReturn(book);

        UpdateFavoriteBookRequest updateRequest = mock(UpdateFavoriteBookRequest.class);
        given(updateRequest.briefReview()).willReturn("briefReview");
        given(updateRequest.fullReview()).willReturn("fullReview");

        doNothing().when(favoriteBook).changeBook(book);
        doNothing().when(favoriteBook).updateTextFields(any(String.class), any(String.class));

        // when
        favoriteBookService.updateFavoriteBook(memberResponse, 1L, updateRequest);

        // then
        verify(memberService).findMemberByIdOrThrow(anyLong());
        verify(favoriteBookRepository).findById(anyLong());
        verify(bookService).findBookByIdOrThrow(anyLong());
        verify(favoriteBook).isWrittenBy(author);
        verify(favoriteBook).changeBook(book);
        verify(favoriteBook).updateTextFields(any(String.class), any(String.class));
    }
}
