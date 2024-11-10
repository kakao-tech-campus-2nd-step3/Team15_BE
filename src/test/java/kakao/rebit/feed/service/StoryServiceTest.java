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
import kakao.rebit.feed.dto.request.update.UpdateStoryRequest;
import kakao.rebit.feed.entity.Story;
import kakao.rebit.feed.repository.StoryRepository;
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
@DisplayName("스토리 서비스 테스트")
class StoryServiceTest {

    @InjectMocks
    StoryService storyService;

    @Mock
    StoryRepository storyRepository;

    @Mock
    BookService bookService;

    @Mock
    MemberService memberService;


    @Test
    void 스토리_수정_테스트() {
        //given
        MemberResponse memberResponse = mock(MemberResponse.class);
        given(memberResponse.id()).willReturn(1L);

        Member author = mock(Member.class);
        given(memberService.findMemberByIdOrThrow(anyLong())).willReturn(author);

        Story story = mock(Story.class);
        given(storyRepository.findById(anyLong())).willReturn(Optional.of(story));
        given(story.isWrittenBy(author)).willReturn(true);

        Book book = mock(Book.class);
        given(bookService.findBookIfBookIdExist(anyLong())).willReturn(Optional.ofNullable(book));
        doNothing().when(story).changeBook(book);

        given(story.getImageKey()).willReturn("image-key");

        UpdateStoryRequest updateRequest = mock(UpdateStoryRequest.class);
        given(updateRequest.imageKey()).willReturn("change-image-key");
        given(updateRequest.content()).willReturn("content");

        doNothing().when(story).changeImageKey(any(String.class));
        doNothing().when(story).updateTextFields(any(String.class));

        // when
        storyService.updateStory(memberResponse, 1L, updateRequest);

        // then
        verify(memberService).findMemberByIdOrThrow(anyLong());
        verify(storyRepository).findById(anyLong());
        verify(bookService).findBookIfBookIdExist(anyLong());
        verify(story).isWrittenBy(author);
        verify(story).getImageKey();
        verify(story).changeBook(book);
        verify(story).changeImageKey(any(String.class));
        verify(story).updateTextFields(any(String.class));
    }
}
