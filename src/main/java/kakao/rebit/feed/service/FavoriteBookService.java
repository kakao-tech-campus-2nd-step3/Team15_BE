package kakao.rebit.feed.service;

import kakao.rebit.feed.dto.response.FeedResponse;
import kakao.rebit.feed.entity.FavoriteBook;
import kakao.rebit.feed.mapper.FeedMapper;
import kakao.rebit.feed.repository.FavoriteBookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteBookService {

    private final FavoriteBookRepository favoriteBookRepository;
    private final FeedMapper feedMapper;

    public FavoriteBookService(FavoriteBookRepository favoriteBookRepository,
            FeedMapper feedMapper) {
        this.favoriteBookRepository = favoriteBookRepository;
        this.feedMapper = feedMapper;
    }

    @Transactional(readOnly = true)
    public Page<FeedResponse> getFavoriteBooks(Pageable pageable) {
        Page<FavoriteBook> favorites = favoriteBookRepository.findAll(pageable);
        System.out.println(favorites);
        return favorites.map(feedMapper::toFeedResponse);
    }

    @Transactional(readOnly = true)
    public FeedResponse getFavoriteBookById(Long id) {
        FavoriteBook favoriteBook = favoriteBookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("찾는 인생책이 없습니다."));
        return feedMapper.toFeedResponse(favoriteBook);
    }
}
