package kakao.rebit.feed.service;

import kakao.rebit.book.entity.Book;
import kakao.rebit.feed.dto.response.BookResponse;
import kakao.rebit.feed.dto.response.MagazineResponse;
import kakao.rebit.feed.entity.Magazine;
import kakao.rebit.feed.repository.MagazineRepository;
import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MagazineService {

    private final MagazineRepository magazineRepository;

    public MagazineService(MagazineRepository magazineRepository) {
        this.magazineRepository = magazineRepository;
    }

    @Transactional(readOnly = true)
    public Page<MagazineResponse> getMagazines(Pageable pageable) {
        return magazineRepository.findAll(pageable).map(this::toMagazineResponse);
    }

    @Transactional(readOnly = true)
    public MagazineResponse getMagazineById(Long id) {
        Magazine magazine = magazineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("찾는 Magazine이 없습니다."));
        return toMagazineResponse(magazine);
    }

    private MagazineResponse toMagazineResponse(Magazine magazine) {
        return new MagazineResponse(
                magazine.getId(),
                toMemberResponse(magazine.getMember()),
                toBookResponse(magazine.getBook()),
                magazine.getType(),
                magazine.getName(),
                magazine.getImageUrl(),
                magazine.getContent()
        );
    }

    /**
     * 아래부터는 DTO 변환 로직
     */

    private MemberResponse toMemberResponse(Member member) {
        return new MemberResponse(member.getId(), member.getNickname(), member.getImageUrl(),
                member.getBio(), member.getRole(), member.getPoint());
    }

    private BookResponse toBookResponse(Book book) {
        return new BookResponse(book.getId(), book.getIsbn(), book.getTitle(),
                book.getDescription(), book.getAuthor(), book.getPublisher(), book.getImageUrl());
    }
}
