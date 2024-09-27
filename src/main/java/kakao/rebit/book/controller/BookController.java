package kakao.rebit.book.controller;

import java.util.List;
import kakao.rebit.book.entity.Book;
import kakao.rebit.book.entity.Review;
import kakao.rebit.book.service.BookService;
import kakao.rebit.book.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {

    private final BookService bookService;
    private final ReviewService reviewService;

    public BookController(BookService bookService, ReviewService reviewService) {
        this.bookService = bookService;
        this.reviewService = reviewService;
    }

    @GetMapping("/books/all")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/books")
    public ResponseEntity<String> getBooks(@RequestParam(name="query") String query) {
        String apiResponse = bookService.searchBooksByTitle(query);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/books/{isbn}")
    public Book getBookDetail(@PathVariable(name = "isbn") String isbn) {
        return bookService.getBookDetail(isbn);
    }

    @GetMapping("/books/{isbn}/brief-reviews")
    public List<Review> getBriefReviews(@PathVariable(name = "isbn") String isbn) {
        return reviewService.getBriefReviews(isbn);
    }
}
