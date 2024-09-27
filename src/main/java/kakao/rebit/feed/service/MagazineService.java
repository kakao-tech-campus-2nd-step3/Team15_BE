package kakao.rebit.feed.service;

import kakao.rebit.feed.dto.response.MagazineResponse;
import kakao.rebit.feed.entity.Magazine;
import kakao.rebit.feed.repository.MagazineRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MagazineService {

    private final MagazineRepository magazineRepository;
    private final FeedService feedService;

    public MagazineService(MagazineRepository magazineRepository, FeedService feedService) {
        this.magazineRepository = magazineRepository;
        this.feedService = feedService;
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
                feedService.toAuthorResponse(magazine.getMember()),
                feedService.toBookResponse(magazine.getBook()),
                magazine.getType(),
                magazine.getName(),
                magazine.getImageUrl(),
                magazine.getContent()
        );
    }
}
