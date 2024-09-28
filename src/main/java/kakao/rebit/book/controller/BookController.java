package kakao.rebit.book.controller;

import java.util.List;
import kakao.rebit.book.entity.Book;
import kakao.rebit.book.service.BookService;
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
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/search")
    public ResponseEntity<String> searchBooksByTitle(@RequestParam(name = "title") String title) {
        String apiResponse = bookService.searchAndSaveBooksByTitle(title);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/search/{isbn}")
    public ResponseEntity<Book> getBookDetail(@PathVariable(name = "isbn") String isbn) {
        Book book = bookService.getBookDetail(isbn);
        return ResponseEntity.ok(book);
    }
}
