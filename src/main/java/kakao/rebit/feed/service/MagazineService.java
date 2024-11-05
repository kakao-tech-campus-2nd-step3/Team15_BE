package kakao.rebit.feed.service;

import java.util.Set;
import kakao.rebit.book.entity.Book;
import kakao.rebit.book.service.BookService;
import kakao.rebit.feed.dto.request.update.UpdateMagazineRequest;
import kakao.rebit.feed.dto.response.MagazineResponse;
import kakao.rebit.feed.entity.Magazine;
import kakao.rebit.feed.exception.feed.FeedNotFoundException;
import kakao.rebit.feed.exception.feed.UpdateNotAuthorizedException;
import kakao.rebit.feed.mapper.FeedMapper;
import kakao.rebit.feed.repository.MagazineRepository;
import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.service.MemberService;
import kakao.rebit.s3.service.S3Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MagazineService {

    private final MagazineRepository magazineRepository;
    private final MemberService memberService;
    private final BookService bookService;
    private final FeedMapper feedMapper;
    private final LikesService likesService;
    private final S3Service s3Service;

    public MagazineService(MagazineRepository magazineRepository, MemberService memberService, BookService bookService, FeedMapper feedMapper,
            LikesService likesService, S3Service s3Service) {
        this.magazineRepository = magazineRepository;
        this.memberService = memberService;
        this.bookService = bookService;
        this.feedMapper = feedMapper;
        this.likesService = likesService;
        this.s3Service = s3Service;
    }

    @Transactional(readOnly = true)
    public Page<MagazineResponse> getMagazines(MemberResponse memberResponse, Pageable pageable) {
        Page<Magazine> feedPage = magazineRepository.findAll(pageable);

        if (memberResponse != null) {
            Member viewer = memberService.findMemberByIdOrThrow(memberResponse.id());
            Set<Long> likedFeedIds = likesService.getLikedFeedIdsByMember(viewer); // 멤버가 좋아요를 누른 모든 피드를 가져온다.

            return feedPage.map(feed -> (MagazineResponse)
                    feedMapper.toFeedResponse(likesService.isLikedBySet(likedFeedIds, feed), feed));
        }

        return feedPage.map(feed -> (MagazineResponse) feedMapper.toFeedResponse(false, feed));
    }

    @Transactional(readOnly = true)
    public Page<MagazineResponse> getMyMagazines(MemberResponse memberResponse, Pageable pageable) {
        Member author = memberService.findMemberByIdOrThrow(memberResponse.id());
        Set<Long> likedFeedIds = likesService.getLikedFeedIdsByMember(author);

        return magazineRepository.findAllByMember(author, pageable)
                .map(feed -> (MagazineResponse) feedMapper.toFeedResponse(likesService.isLikedBySet(likedFeedIds, feed), feed));
    }

    @Transactional(readOnly = true)
    public MagazineResponse getMagazineById(MemberResponse memberResponse, Long magazineId) {
        Member viewer = memberService.findMemberByIdOrThrow(memberResponse.id());
        Magazine magazine = findMagazineByIdOrThrow(magazineId);
        return (MagazineResponse) feedMapper.toFeedResponse(likesService.isLiked(viewer, magazine), magazine);
    }

    @Transactional(readOnly = true)
    public Magazine findMagazineByIdOrThrow(Long magazineId) {
        return magazineRepository.findById(magazineId)
                .orElseThrow(() -> FeedNotFoundException.EXCEPTION);
    }

    @Transactional
    public void updateMagazine(MemberResponse memberResponse, Long magazineId, UpdateMagazineRequest updateRequest) {
        Member author = memberService.findMemberByIdOrThrow(memberResponse.id());
        Magazine magazine = findMagazineByIdOrThrow(magazineId);

        if (!magazine.isWrittenBy(author)) {
            throw UpdateNotAuthorizedException.EXCEPTION;
        }

        Book book = bookService.findBookIfBookIdExist(updateRequest.bookId()).orElse(null);
        magazine.changeBook(book);

        String preImageKey = magazine.getImageKey(); // 변경 전 imageKey 값 저장
        magazine.changeImageKey(updateRequest.imageKey());

        magazine.updateTextFields(updateRequest.name(), updateRequest.content());

        // 이미지가 수정됐으면 기존의 S3에서 이전 이미지 삭제하기
        if (magazine.isImageKeyUpdated(preImageKey)) {
            s3Service.deleteObject(preImageKey);
        }
    }
}
