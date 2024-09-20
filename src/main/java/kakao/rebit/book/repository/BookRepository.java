package kakao.rebit.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import kakao.rebit.book.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}