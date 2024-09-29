package kakao.rebit.feed.service;

import kakao.rebit.feed.dto.response.MagazineResponse;
import kakao.rebit.feed.entity.Magazine;
import kakao.rebit.feed.mapper.FeedMapper;
import kakao.rebit.feed.repository.MagazineRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MagazineService {

    private final MagazineRepository magazineRepository;
    private final FeedMapper feedMapper;

    public MagazineService(MagazineRepository magazineRepository, FeedMapper feedMapper) {
        this.magazineRepository = magazineRepository;
        this.feedMapper = feedMapper;
    }

    @Transactional(readOnly = true)
    public Page<MagazineResponse> getMagazines(Pageable pageable) {
        return magazineRepository.findAll(pageable).map(feedMapper::toMagazineResponse);
    }

    @Transactional(readOnly = true)
    public MagazineResponse getMagazineById(Long id) {
        Magazine magazine = magazineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("찾는 메거진이 없습니다."));
        return feedMapper.toMagazineResponse(magazine);
    }
}
