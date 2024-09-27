package kakao.rebit.book.repository;

import kakao.rebit.book.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByIsbn(String isbn);
}
