package kakao.rebit.feed.service;

import kakao.rebit.feed.dto.response.FavoriteBookResponse;
import kakao.rebit.feed.entity.FavoriteBook;
import kakao.rebit.feed.repository.FavoriteBookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteBookService {

    private final FavoriteBookRepository favoriteBookRepository;
    private final FeedService feedService;

    public FavoriteBookService(FavoriteBookRepository favoriteBookRepository,
            FeedService feedService) {
        this.favoriteBookRepository = favoriteBookRepository;
        this.feedService = feedService;
    }

    @Transactional(readOnly = true)
    public Page<FavoriteBookResponse> getFavoriteBooks(Pageable pageable) {
        Page<FavoriteBook> favorites = favoriteBookRepository.findAll(pageable);
        System.out.println(favorites);
        return favorites.map(this::toFavoriteBookResponse);
    }

    @Transactional(readOnly = true)
    public FavoriteBookResponse getFavoriteBookById(Long id) {
        FavoriteBook favoriteBook = favoriteBookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("찾는 인생책이 없습니다."));
        return toFavoriteBookResponse(favoriteBook);
    }

    /**
     * 아래부터는 DTO 변환 로직
     */

    private FavoriteBookResponse toFavoriteBookResponse(FavoriteBook favoriteBook) {
        return new FavoriteBookResponse(favoriteBook.getId(),
                feedService.toAuthorResponse(favoriteBook.getMember()),
                feedService.toBookResponse(favoriteBook.getBook()),
                favoriteBook.getType(), favoriteBook.getBriefReview(),
                favoriteBook.getFullReview());
    }
}
