package kakao.rebit.book.controller;

import java.util.List;
import kakao.rebit.book.service.BookService;
import kakao.rebit.feed.dto.response.BookResponse;
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
    public ResponseEntity<List<BookResponse>> searchBooksByTitle(@RequestParam(name = "title") String title) {
        List<BookResponse> books = bookService.searchAndSaveBooksByTitle(title);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<BookResponse> getBookDetail(@PathVariable(name = "isbn") String isbn) {
        BookResponse book = bookService.getBookDetail(isbn);
        return ResponseEntity.ok(book);
    }
}
