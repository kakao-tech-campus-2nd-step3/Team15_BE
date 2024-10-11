package kakao.rebit.feed.service;

import kakao.rebit.feed.dto.response.FeedResponse;
import kakao.rebit.feed.entity.Magazine;
import kakao.rebit.feed.exception.feed.FeedNotFoundException;
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
    public Page<FeedResponse> getMagazines(Pageable pageable) {
        return magazineRepository.findAll(pageable).map(feedMapper::toFeedResponse);
    }

    @Transactional(readOnly = true)
    public FeedResponse getMagazineById(Long id) {
        Magazine magazine = magazineRepository.findById(id)
                .orElseThrow(() -> FeedNotFoundException.EXCEPTION);
        return feedMapper.toFeedResponse(magazine);
    }
}
