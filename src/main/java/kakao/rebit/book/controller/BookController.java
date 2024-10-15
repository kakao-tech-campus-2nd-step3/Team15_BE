package kakao.rebit.book.controller;

import java.util.List;
import kakao.rebit.book.dto.BookDetailResponse;
import kakao.rebit.book.dto.BookResponse;
import kakao.rebit.book.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        List<BookResponse> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<BookResponse>> searchBooksByTitle(
        @RequestParam(name = "title") String title, Pageable pageable) {
        Page<BookResponse> books = bookService.searchAndSaveBooksByTitle(title, pageable);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<BookResponse> getBookDetail(@PathVariable(name = "isbn") String isbn) {
        BookResponse book = bookService.getBookDetail(isbn);
        return ResponseEntity.ok(book);
    }

    // ISBN으로 해당 책의 작성 된 한줄평을 모두 조회
    @GetMapping("/{isbn}/brief-reviews")
    public ResponseEntity<Page<String>> getAllBriefReviews(@PathVariable("isbn") String isbn,
        Pageable pageable) {
        Page<String> briefReviews = bookService.getBriefReviewsByIsbn(isbn, pageable);
        return ResponseEntity.ok(briefReviews);
    }

    // 좋아요가 가장 많은 책의 한줄평과 서평을 포함한 상세 정보 조회
    @GetMapping("/detail/{isbn}")
    public ResponseEntity<BookDetailResponse> getBookDetailReview(
        @PathVariable(name = "isbn") String isbn) {
        BookDetailResponse bookDetail = bookService.getBookDetailReview(isbn);
        return ResponseEntity.ok(bookDetail);
    }
}
