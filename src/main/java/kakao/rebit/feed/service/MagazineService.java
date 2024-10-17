package kakao.rebit.feed.service;

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
    private final S3Service s3Service;

    public MagazineService(MagazineRepository magazineRepository, MemberService memberService,
            BookService bookService, FeedMapper feedMapper, S3Service s3Service) {
        this.magazineRepository = magazineRepository;
        this.memberService = memberService;
        this.bookService = bookService;
        this.feedMapper = feedMapper;
        this.s3Service = s3Service;
    }

    @Transactional(readOnly = true)
    public Page<MagazineResponse> getMagazines(Pageable pageable) {
        Page<Magazine> magazines = magazineRepository.findAll(pageable);
        return magazines.map(magazine -> (MagazineResponse) feedMapper.toFeedResponse(magazine));
    }

    @Transactional(readOnly = true)
    public MagazineResponse getMagazineById(Long magazineId) {
        Magazine magazine = findMagazineByIdOrThrow(magazineId);
        return (MagazineResponse) feedMapper.toFeedResponse(magazine);
    }

    @Transactional(readOnly = true)
    public Magazine findMagazineByIdOrThrow(Long magazineId) {
        return magazineRepository.findById(magazineId)
                .orElseThrow(() -> FeedNotFoundException.EXCEPTION);
    }

    @Transactional
    public void updateMagazine(MemberResponse memberResponse, Long magazineId,
            UpdateMagazineRequest updateRequest) {
        Member member = memberService.findMemberByIdOrThrow(memberResponse.id());
        Magazine magazine = findMagazineByIdOrThrow(magazineId);

        if (!magazine.isWrittenBy(member)) {
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
