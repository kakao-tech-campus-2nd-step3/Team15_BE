package kakao.rebit.feed.service;

import kakao.rebit.book.entity.Book;
import kakao.rebit.feed.dto.response.BookResponse;
import kakao.rebit.feed.dto.response.FavoriteBookResponse;
import kakao.rebit.feed.entity.FavoriteBook;
import kakao.rebit.feed.repository.FavoriteBookRepository;
import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteBookService {

    private final FavoriteBookRepository favoriteBookRepository;

    public FavoriteBookService(FavoriteBookRepository favoriteBookRepository) {
        this.favoriteBookRepository = favoriteBookRepository;
    }

    @Transactional(readOnly = true)
    public Page<FavoriteBookResponse> getFavorites(Pageable pageable) {
        Page<FavoriteBook> favorites = favoriteBookRepository.findAll(pageable);
        System.out.println(favorites);
        return favorites.map(this::toFavoriteBookResponse);
    }

    @Transactional(readOnly = true)
    public FavoriteBookResponse getFavoriteById(Long id) {
        FavoriteBook favoriteBook = favoriteBookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("찾는 인생책이 없습니다."));
        return toFavoriteBookResponse(favoriteBook);
    }

    /**
     * 아래부터는 DTO 변환 로직
     */

    private FavoriteBookResponse toFavoriteBookResponse(FavoriteBook favoriteBook) {
        return new FavoriteBookResponse(favoriteBook.getId(),
                toMemberResponse(favoriteBook.getMember()), toBookResponse(favoriteBook.getBook()),
                favoriteBook.getType(), favoriteBook.getBriefReview(),
                favoriteBook.getFullReview());
    }

    private MemberResponse toMemberResponse(Member member) {
        return new MemberResponse(member.getId(), member.getNickname(), member.getImageUrl(),
                member.getBio(), member.getRole(), member.getPoint());
    }

    private BookResponse toBookResponse(Book book) {
        return new BookResponse(book.getId(), book.getIsbn(), book.getTitle(),
                book.getDescription(), book.getAuthor(), book.getPublisher(), book.getImageUrl());
    }
}
