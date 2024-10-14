package kakao.rebit.feed.service;

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
    private final S3Service s3Service;

    public StoryService(StoryRepository storyRepository, MemberService memberService,
            BookService bookService, FeedMapper feedMapper, S3Service s3Service) {
        this.storyRepository = storyRepository;
        this.memberService = memberService;
        this.bookService = bookService;
        this.feedMapper = feedMapper;
        this.s3Service = s3Service;
    }

    @Transactional(readOnly = true)
    public Page<StoryResponse> getStories(Pageable pageable) {
        Page<Story> stories = storyRepository.findAll(pageable);
        return stories.map(story -> (StoryResponse) feedMapper.toFeedResponse(story));
    }

    @Transactional(readOnly = true)
    public StoryResponse getStoryById(Long storyId) {
        Story story = findStoryByIdOrThrow(storyId);
        return (StoryResponse) feedMapper.toFeedResponse(story);
    }

    @Transactional(readOnly = true)
    public Story findStoryByIdOrThrow(Long magazineId) {
        return storyRepository.findById(magazineId)
                .orElseThrow(() -> FeedNotFoundException.EXCEPTION);
    }

    @Transactional
    public void updateStory(MemberResponse memberResponse, Long storyId,
            UpdateStoryRequest updateRequest) {
        Member member = memberService.findMemberByIdOrThrow(memberResponse.id());
        Story story = findStoryByIdOrThrow(storyId);

        if (!story.isWrittenBy(member)) {
            throw UpdateNotAuthorizedException.EXCEPTION;
        }

        Book book = bookService.findBookIfBookIdExist(updateRequest.bookId()).orElse(null);
        story.changeBook(book);

        // 이미지가 수정됐으면 기존의 S3에서 이전 이미지 삭제하기
        if (story.isImageKeyUpdated(updateRequest.imageKey())) {
            s3Service.deleteObject(story.getImageKey());
        }

        story.changeImageKey(updateRequest.imageKey());
        story.updateTextFields(updateRequest.content());
    }
}
