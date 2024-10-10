package kakao.rebit.feed.repository;

import java.util.Optional;
import kakao.rebit.book.entity.Book;
import kakao.rebit.feed.entity.FavoriteBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteBookRepository extends JpaRepository<FavoriteBook, Long> {
    // 해당 책에 대해 가장 많은 좋아요를 받은 인생책 피드를 반환
    Optional<FavoriteBook> findTopByBookOrderByLikesDesc(Book book);
}
