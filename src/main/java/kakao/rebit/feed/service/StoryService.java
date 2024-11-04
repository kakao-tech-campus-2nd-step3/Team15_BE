package kakao.rebit.feed.service;

import java.util.Set;
import kakao.rebit.book.entity.Book;
import kakao.rebit.book.service.BookService;
import kakao.rebit.feed.dto.request.update.UpdateStoryRequest;
import kakao.rebit.feed.dto.response.StoryResponse;
import kakao.rebit.feed.entity.Story;
import kakao.rebit.feed.exception.feed.FeedNotFoundException;
import kakao.rebit.feed.exception.feed.UpdateNotAuthorizedException;
import kakao.rebit.feed.mapper.FeedMapper;
import kakao.rebit.feed.repository.StoryRepository;
import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.service.MemberService;
import kakao.rebit.s3.service.S3Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StoryService {

    private final StoryRepository storyRepository;
    private final MemberService memberService;
    private final BookService bookService;
    private final FeedMapper feedMapper;
    private final LikesService likesService;
    private final S3Service s3Service;

    public StoryService(StoryRepository storyRepository, MemberService memberService, BookService bookService, FeedMapper feedMapper,
            LikesService likesService, S3Service s3Service) {
        this.storyRepository = storyRepository;
        this.memberService = memberService;
        this.bookService = bookService;
        this.feedMapper = feedMapper;
        this.likesService = likesService;
        this.s3Service = s3Service;
    }

    @Transactional(readOnly = true)
    public Page<StoryResponse> getStories(MemberResponse memberResponse, Pageable pageable) {
        Page<Story> feedPage = storyRepository.findAll(pageable);

        if (memberResponse != null) {
            Member viewer = memberService.findMemberByIdOrThrow(memberResponse.id());
            Set<Long> likedFeedIds = likesService.getLikedFeedIdsByMember(viewer); // 멤버가 좋아요를 누른 모든 피드를 가져온다.

            return feedPage.map(feed -> (StoryResponse)
                    feedMapper.toFeedResponse(likesService.isLikedBySet(likedFeedIds, feed), feed));
        }

        return feedPage.map(feed -> (StoryResponse) feedMapper.toFeedResponse(false, feed));
    }

    @Transactional(readOnly = true)
    public StoryResponse getStoryById(MemberResponse memberResponse, Long storyId) {
        Member viewer = memberService.findMemberByIdOrThrow(memberResponse.id());
        Story story = findStoryByIdOrThrow(storyId);
        return (StoryResponse) feedMapper.toFeedResponse(likesService.isLiked(viewer, story), story);
    }

    @Transactional(readOnly = true)
    public Story findStoryByIdOrThrow(Long magazineId) {
        return storyRepository.findById(magazineId).orElseThrow(() -> FeedNotFoundException.EXCEPTION);
    }

    @Transactional
    public void updateStory(MemberResponse memberResponse, Long storyId, UpdateStoryRequest updateRequest) {
        Member author = memberService.findMemberByIdOrThrow(memberResponse.id());
        Story story = findStoryByIdOrThrow(storyId);

        if (!story.isWrittenBy(author)) {
            throw UpdateNotAuthorizedException.EXCEPTION;
        }

        Book book = bookService.findBookIfBookIdExist(updateRequest.bookId()).orElse(null);
        story.changeBook(book);

        String preImageKey = story.getImageKey(); // 변경 전 imageKey 값 저장
        story.changeImageKey(updateRequest.imageKey());

        // 이미지가 수정됐으면 기존의 S3에서 이전 이미지 삭제하기
        if (story.isImageKeyUpdated(preImageKey)) {
            s3Service.deleteObject(preImageKey);
        }

        story.updateTextFields(updateRequest.content());
    }
}
