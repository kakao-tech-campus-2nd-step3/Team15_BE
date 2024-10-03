package kakao.rebit.book.service;

import java.util.List;
import java.util.stream.Collectors;
import kakao.rebit.book.dto.AladinApiResponseListResponse;
import kakao.rebit.book.dto.AladinApiResponseResponse;
import kakao.rebit.book.entity.Book;
import kakao.rebit.book.repository.BookRepository;
import kakao.rebit.feed.dto.response.BookResponse;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AladinApiService aladinApiService;

    public BookService(BookRepository bookRepository, AladinApiService aladinApiService) {
        this.bookRepository = bookRepository;
        this.aladinApiService = aladinApiService;
    }

    // 전체 책 조회
    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll().stream()
            .map(this::convertToBookResponse)
            .collect(Collectors.toList());
    }

    // 책 타이틀로 검색 후 저장
    public List<BookResponse> searchAndSaveBooksByTitle(String title) {
        AladinApiResponseListResponse bookList = aladinApiService.searchBooksByTitle(title);

        List<Book> savedBooks = bookList.item().stream()
            .filter(book -> bookRepository.findByIsbn(book.isbn()).isEmpty())
            .map(this::convertToBookEntity)
            .map(bookRepository::save)
            .collect(Collectors.toList());

        return savedBooks.stream()
            .map(this::convertToBookResponse)
            .collect(Collectors.toList());
    }

    public BookResponse searchAndSaveBookByIsbn(String isbn) {
        AladinApiResponseResponse bookResponse = aladinApiService.searchBookByIsbn(isbn);
        Book book = bookRepository.findByIsbn(bookResponse.isbn())
            .orElseGet(() -> saveBook(bookResponse));
        return convertToBookResponse(book);
    }

    public BookResponse getBookDetail(String isbn) {
        return bookRepository.findByIsbn(isbn)
            .map(this::convertToBookResponse)
            .orElseGet(() -> searchAndSaveBookByIsbn(isbn));
    }

    private BookResponse convertToBookResponse(Book book) {
        return new BookResponse(
            book.getId(),
            book.getIsbn(),
            book.getTitle(),
            book.getAuthor(),
            book.getCover(),
            book.getDescription(),
            book.getPublisher(),
            book.getPubDate()
        );
    }

    private Book convertToBookEntity(AladinApiResponseResponse response) {
        return new Book(
            response.isbn(),
            response.title(),
            response.description(),
            response.author(),
            response.publisher(),
            response.cover(),
            response.pubDate()
        );
    }

    private Book saveBook(AladinApiResponseResponse bookResponse) {
        return bookRepository.save(convertToBookEntity(bookResponse));
    }

    public Book findBookByIdOrThrow(Long bookId) {
        return bookRepository.findById(bookId)
            .orElseThrow(() -> new IllegalArgumentException("해당 책이 존재하지 않습니다."));
    }
}

