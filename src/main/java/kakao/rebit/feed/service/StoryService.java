package kakao.rebit.feed.service;

import java.util.Optional;
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

    public StoryService(StoryRepository storyRepository, MemberService memberService, BookService bookService, FeedMapper feedMapper,
            S3Service s3Service) {
        this.storyRepository = storyRepository;
        this.memberService = memberService;
        this.bookService = bookService;
        this.feedMapper = feedMapper;
        this.s3Service = s3Service;
    }

    @Transactional(readOnly = true)
    public Page<StoryResponse> getStories(MemberResponse memberResponse, Pageable pageable) {
        Optional<Member> viewer = Optional.ofNullable(memberResponse).map(response -> memberService.findMemberByIdOrThrow(response.id()));
        return storyRepository.findAll(pageable).map(story -> (StoryResponse) feedMapper.toFeedResponse(viewer.orElse(null), story));
    }

    @Transactional(readOnly = true)
    public StoryResponse getStoryById(MemberResponse memberResponse, Long storyId) {
        Member viewer = memberService.findMemberByIdOrThrow(memberResponse.id());
        Story story = findStoryByIdOrThrow(storyId);
        return (StoryResponse) feedMapper.toFeedResponse(viewer, story);
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
