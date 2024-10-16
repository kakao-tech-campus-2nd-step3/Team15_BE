package kakao.rebit.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "책 API", description = "책 관련 API")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "전체 책 목록 조회", description = "서버에 저장된 모든 책 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        List<BookResponse> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "책 제목으로 검색", description = "제목을 기준으로 책을 검색하고 결과를 페이지네이션 처리하여 반환합니다.")
    @GetMapping("/search")
    public ResponseEntity<Page<BookResponse>> searchBooksByTitle(
        @Parameter(description = "검색할 책 제목") @RequestParam(name = "title") String title,
        Pageable pageable) {
        Page<BookResponse> books = bookService.searchAndSaveBooksByTitle(title, pageable);
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "책 상세 정보 조회", description = "ISBN을 기준으로 책의 상세 정보를 조회합니다.")
    @GetMapping("/{isbn}")
    public ResponseEntity<BookResponse> getBookDetail(@PathVariable(name = "isbn") String isbn) {
        BookResponse book = bookService.getBookDetail(isbn);
        return ResponseEntity.ok(book);
    }

    // ISBN으로 해당 책의 작성 된 한줄평을 모두 조회
    @Operation(summary = "책의 한줄평 조회", description = "ISBN을 기준으로 해당 책의 모든 한줄평을 페이지네이션 처리하여 조회합니다.")
    @GetMapping("/{isbn}/brief-reviews")
    public ResponseEntity<Page<String>> getAllBriefReviews(@PathVariable("isbn") String isbn,
        Pageable pageable) {
        Page<String> briefReviews = bookService.getBriefReviewsByIsbn(isbn, pageable);
        return ResponseEntity.ok(briefReviews);
    }

    // 좋아요가 가장 많은 책의 한줄평과 서평을 포함한 상세 정보 조회
    @Operation(summary = "책의 상세 정보 및 서평 조회", description = "ISBN을 기준으로 좋아요가 가장 많은 서평을 포함한 책의 상세 정보를 조회합니다.")
    @GetMapping("/detail/{isbn}")
    public ResponseEntity<BookDetailResponse> getBookDetailReview(
        @PathVariable(name = "isbn") String isbn) {
        BookDetailResponse bookDetail = bookService.getBookDetailReview(isbn);
        return ResponseEntity.ok(bookDetail);
    }
}
