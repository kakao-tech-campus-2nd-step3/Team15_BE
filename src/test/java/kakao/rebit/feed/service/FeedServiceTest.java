package kakao.rebit.feed.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import kakao.rebit.book.entity.Book;
import kakao.rebit.book.service.BookService;
import kakao.rebit.feed.dto.request.create.CreateFeedRequest;
import kakao.rebit.feed.dto.response.FeedResponse;
import kakao.rebit.feed.dto.response.LikesMemberResponse;
import kakao.rebit.feed.entity.FavoriteBook;
import kakao.rebit.feed.entity.Feed;
import kakao.rebit.feed.entity.Magazine;
import kakao.rebit.feed.entity.Story;
import kakao.rebit.feed.mapper.FeedMapper;
import kakao.rebit.feed.repository.FeedRepository;
import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.service.MemberService;
import kakao.rebit.s3.service.S3Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
@DisplayName("피드 서비스 테스트")
class FeedServiceTest {

    @InjectMocks
    FeedService feedService;

    @Mock
    FeedRepository feedRepository;

    @Mock
    FeedMapper feedMapper;

    @Mock
    MemberService memberService;

    @Mock
    BookService bookService;

    @Mock
    S3Service s3Service;

    @Mock
    LikesService likesService;

    @Test
    void 모든_피드_조회() {
        //given
        Feed feed = mock(Feed.class);
        Page<Feed> feedPage = new PageImpl<>(List.of(feed));
        given(feedRepository.findAll(any(Pageable.class))).willReturn(feedPage);

        MemberResponse memberResponse = mock(MemberResponse.class);
        given(memberResponse.id()).willReturn(1L);

        Member viewer = mock(Member.class);
        given(memberService.findMemberByIdOrThrow(anyLong())).willReturn(viewer);

        Set<Long> likedFeedIds = Set.of(1L, 2L);
        given(likesService.getLikedFeedIdsByMember(viewer)).willReturn(likedFeedIds);
        given(likesService.isLikedBySet(likedFeedIds, feed)).willReturn(true);

        FeedResponse feedResponse = mock(FeedResponse.class);
        given(feedMapper.toFeedResponse(true, feed)).willReturn(feedResponse);

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<FeedResponse> result = feedService.getFeeds(memberResponse, pageable);

        // then
        verify(feedRepository).findAll(any(Pageable.class));
        verify(memberService).findMemberByIdOrThrow(anyLong());
        verify(likesService).getLikedFeedIdsByMember(viewer);
        verify(likesService).isLikedBySet(likedFeedIds, feed);
        verify(feedMapper).toFeedResponse(true, feed);

        assertNotNull(result);
    }

    @Test
    void 내가_작성한_모든_피드를_조회한다() {
        // given
        MemberResponse memberResponse = mock(MemberResponse.class);
        given(memberResponse.id()).willReturn(1L);

        Member author = mock(Member.class);
        given(memberService.findMemberByIdOrThrow(anyLong())).willReturn(author);

        Feed feed = mock(Feed.class);
        Page<Feed> feedPage = new PageImpl<>(List.of(feed));
        Pageable pageable = PageRequest.of(0, 10);
        given(feedRepository.findAllByMember(author, pageable)).willReturn(feedPage);

        Set<Long> likedFeedIds = Set.of(1L, 2L);
        given(likesService.getLikedFeedIdsByMember(author)).willReturn(likedFeedIds);
        given(likesService.isLikedBySet(likedFeedIds, feed)).willReturn(true);

        FeedResponse feedResponse = mock(FeedResponse.class);
        given(feedMapper.toFeedResponse(true, feed)).willReturn(feedResponse);

        // when
        Page<FeedResponse> result = feedService.getMyFeeds(memberResponse, pageable);

        // then
        verify(memberService).findMemberByIdOrThrow(anyLong());
        verify(likesService).getLikedFeedIdsByMember(author);
        verify(feedRepository).findAllByMember(author, pageable);
        verify(likesService).isLikedBySet(likedFeedIds, feed);
        verify(feedMapper).toFeedResponse(true, feed);

        assertNotNull(result);
    }

    @Test
    void 해당_피드_조회() {
        // given
        MemberResponse memberResponse = mock(MemberResponse.class);
        given(memberResponse.id()).willReturn(1L);

        Member viewer = mock(Member.class);
        given(memberService.findMemberByIdOrThrow(anyLong())).willReturn(viewer);

        Feed feed = mock(Feed.class);
        given(feedRepository.findById(anyLong())).willReturn(Optional.ofNullable(feed));

        given(likesService.isLiked(viewer, feed)).willReturn(true);

        FeedResponse feedResponse = mock(FeedResponse.class);
        given(feedMapper.toFeedResponse(true, feed)).willReturn(feedResponse);

        // when
        FeedResponse result = feedService.getFeedById(memberResponse, 1L);

        // then
        verify(memberService).findMemberByIdOrThrow(1L);
        verify(feedRepository).findById(anyLong());
        verify(likesService).isLiked(viewer, feed);
        verify(feedMapper).toFeedResponse(true, feed);

        assertNotNull(result); // 반환값이 null이 아님을 확인
    }

    @Test
    void 피드_작성() {
        MemberResponse memberResponse = mock(MemberResponse.class);
        given(memberResponse.id()).willReturn(1L);

        Member author = mock(Member.class);
        given(memberService.findMemberByIdOrThrow(anyLong())).willReturn(author);

        CreateFeedRequest feedRequest = mock(CreateFeedRequest.class);
        given(feedRequest.getBookId()).willReturn(1L);

        Book book = mock(Book.class);
        given(bookService.findBookByIdOrThrow(anyLong())).willReturn(book);

        Feed feed = mock(Feed.class);
        given(feedMapper.toFeed(author, book, feedRequest)).willReturn(feed);
        given(feedRepository.save(feed)).willReturn(feed);

        given(feed.getId()).willReturn(1L);

        // when
        Long result = feedService.createFeed(memberResponse, feedRequest);

        // then
        verify(memberService).findMemberByIdOrThrow(anyLong());
        verify(bookService).findBookByIdOrThrow(anyLong());
        verify(feedMapper).toFeed(author, book, feedRequest);
        verify(feedRepository).save(feed);

        assertEquals(1L, result);
    }

    @Test
    void 인생책_피드_삭제() {
        // given
        MemberResponse memberResponse = mock(MemberResponse.class);
        given(memberResponse.id()).willReturn(1L);

        Member author = mock(Member.class);
        given(memberService.findMemberByIdOrThrow(anyLong())).willReturn(author);

        FavoriteBook feed = mock(FavoriteBook.class);
        given(feedRepository.findById(anyLong())).willReturn(Optional.ofNullable(feed));
        given(feed.isWrittenBy(author)).willReturn(true);

        doNothing().when(feedRepository).deleteById(anyLong());

        // when
        feedService.deleteFeedById(memberResponse, 1L);

        // then
        verify(memberService).findMemberByIdOrThrow(anyLong());
        verify(feed).isWrittenBy(author);
        verify(feedRepository).deleteById(anyLong());
    }

    @Test
    void 매거진_피드_삭제() {
        // given
        MemberResponse memberResponse = mock(MemberResponse.class);
        given(memberResponse.id()).willReturn(1L);

        Member author = mock(Member.class);
        given(memberService.findMemberByIdOrThrow(anyLong())).willReturn(author);

        Magazine feed = mock(Magazine.class);
        given(feedRepository.findById(anyLong())).willReturn(Optional.ofNullable(feed));
        given(feed.isWrittenBy(author)).willReturn(true);

        doNothing().when(feedRepository).deleteById(anyLong());
        doNothing().when(s3Service).deleteObject(any());

        // when
        feedService.deleteFeedById(memberResponse, 1L);

        // then
        verify(memberService).findMemberByIdOrThrow(anyLong());
        verify(feed).isWrittenBy(author);
        verify(feedRepository).deleteById(anyLong());
        verify(s3Service).deleteObject(feed.getImageKey());
    }

    @Test
    void 스토리_피드_삭제() {
        // given
        MemberResponse memberResponse = mock(MemberResponse.class);
        given(memberResponse.id()).willReturn(1L);

        Member author = mock(Member.class);
        given(memberService.findMemberByIdOrThrow(anyLong())).willReturn(author);

        Story feed = mock(Story.class);
        given(feedRepository.findById(anyLong())).willReturn(Optional.ofNullable(feed));
        given(feed.isWrittenBy(author)).willReturn(true);

        doNothing().when(feedRepository).deleteById(anyLong());
        doNothing().when(s3Service).deleteObject(any());

        // when
        feedService.deleteFeedById(memberResponse, 1L);

        // then
        verify(memberService).findMemberByIdOrThrow(anyLong());
        verify(feed).isWrittenBy(author);
        verify(feedRepository).deleteById(anyLong());
        verify(s3Service).deleteObject(feed.getImageKey());
    }

    @Test
    void 해당_피드에_좋아요를_누른_모든_멤버_조회() {
        // given
        MemberResponse memberResponse = mock(MemberResponse.class);
        given(memberResponse.id()).willReturn(1L);

        Member author = mock(Member.class);
        given(memberService.findMemberByIdOrThrow(anyLong())).willReturn(author);

        Feed feed = mock(Feed.class);
        given(feedRepository.findById(anyLong())).willReturn(Optional.ofNullable(feed));
        given(feed.isWrittenBy(author)).willReturn(true);

        Pageable pageable = PageRequest.of(0, 10);
        Page<LikesMemberResponse> likesMembers = mock(Page.class);
        given(likesService.findLikesMembers(feed, pageable)).willReturn(likesMembers);

        // when
        Page<LikesMemberResponse> result = feedService.getLikesMembers(memberResponse, 1L, pageable);

        // then
        verify(memberService).findMemberByIdOrThrow(anyLong());
        verify(feedRepository).findById(anyLong());
        verify(feed).isWrittenBy(author);
        verify(likesService).findLikesMembers(feed, pageable);

        assertEquals(likesMembers, result);
    }

    @Test
    void 좋아요_추가() {
        // given
        MemberResponse memberResponse = mock(MemberResponse.class);
        given(memberResponse.id()).willReturn(1L);

        Member member = mock(Member.class);
        given(memberService.findMemberByIdOrThrow(anyLong())).willReturn(member);

        Feed feed = mock(Feed.class);
        given(feedRepository.findById(anyLong())).willReturn(Optional.ofNullable(feed));

        given(likesService.createLikes(member, feed)).willReturn(1L);

        Long expected = 1L;

        // when
        Long result = feedService.createLikes(memberResponse, expected);

        // then
        verify(memberService).findMemberByIdOrThrow(anyLong());
        verify(feedRepository).findById(anyLong());
        verify(likesService).createLikes(member, feed);

        assertEquals(expected, result);
    }

    @Test
    void 좋아요_삭제() {
        // given
        MemberResponse memberResponse = mock(MemberResponse.class);
        given(memberResponse.id()).willReturn(1L);

        Member member = mock(Member.class);
        given(memberService.findMemberByIdOrThrow(anyLong())).willReturn(member);

        Feed feed = mock(Feed.class);
        given(feedRepository.findById(anyLong())).willReturn(Optional.ofNullable(feed));

        doNothing().when(likesService).deleteLikes(member, feed);

        // when
        feedService.deleteLikes(memberResponse, 1L);

        // then
        verify(memberService).findMemberByIdOrThrow(anyLong());
        verify(feedRepository).findById(anyLong());
        verify(likesService).deleteLikes(member, feed);
    }
}
