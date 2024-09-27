package kakao.rebit.book.controller;

import kakao.rebit.book.entity.Book;
import kakao.rebit.book.entity.Review;
import kakao.rebit.book.repository.BookRepository;
import kakao.rebit.book.service.AladinApiService;
import kakao.rebit.book.service.BookService;
import kakao.rebit.book.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class BookController {

    private final BookService bookService;
    private final ReviewService reviewService;
    private final AladinApiService aladinApiService;
    private final BookRepository bookRepository; // 추가

    @Autowired
    public BookController(BookService bookService, ReviewService reviewService, AladinApiService aladinApiService,BookRepository bookRepository) {
        this.bookService = bookService;
        this.reviewService = reviewService;
        this.aladinApiService = aladinApiService;
        this.bookRepository = bookRepository;
    }

    @GetMapping("/books/all")
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    @GetMapping("/books")
    public ResponseEntity<String> getBooks(@RequestParam(name="query") String query) {
        String apiResponse = aladinApiService.searchBookByTitle(query);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/books/{isbn}")
    public Book getBookDetail(@PathVariable(name = "isbn") String isbn) {
        return bookService.getBookDetail(isbn);
    }
}
