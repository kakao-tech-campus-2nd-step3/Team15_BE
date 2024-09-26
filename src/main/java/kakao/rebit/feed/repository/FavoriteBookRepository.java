package kakao.rebit.feed.repository;

import kakao.rebit.feed.entity.FavoriteBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteBookRepository extends JpaRepository<FavoriteBook, Long> {

}
