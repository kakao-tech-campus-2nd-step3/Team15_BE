package kakao.rebit.book.service;

import java.util.List;
import java.util.stream.Collectors;
import kakao.rebit.book.dto.AladinApiResponseListResponse;
import kakao.rebit.book.dto.AladinApiResponseResponse;
import kakao.rebit.book.dto.BookResponse;
import kakao.rebit.book.entity.Book;
import kakao.rebit.book.exception.book.BookNotFoundException;
import kakao.rebit.book.mapper.BookMapper;
import kakao.rebit.book.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AladinApiService aladinApiService;

    public BookService(BookRepository bookRepository, AladinApiService aladinApiService) {
        this.bookRepository = bookRepository;
        this.aladinApiService = aladinApiService;
    }

    @Transactional(readOnly = true)
    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll().stream()
            .map(BookMapper::toBookResponse)
            .collect(Collectors.toList());
    }

    // 책 타이틀로 검색 후 저장
    @Transactional
    public List<BookResponse> searchAndSaveBooksByTitle(String title) {
        AladinApiResponseListResponse bookList = aladinApiService.searchBooksByTitle(title);

        List<Book> savedBooks = bookList.item().stream()
            .filter(book -> bookRepository.findByIsbn(book.isbn()).isEmpty())
            .map(BookMapper::toBookEntity)
            .map(bookRepository::save)
            .toList();

        return savedBooks.stream()
            .map(BookMapper::toBookResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public BookResponse getBookDetail(String isbn) {
        Book book = searchAndSaveBookByIsbn(isbn);
        return BookMapper.toBookResponse(book);
    }

    private Book searchAndSaveBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn)
            .orElseGet(() -> {
                AladinApiResponseResponse bookResponse = aladinApiService.searchBookByIsbn(isbn);
                return saveBook(bookResponse);
            });
    }

    private Book saveBook(AladinApiResponseResponse bookResponse) {
        return bookRepository.save(BookMapper.toBookEntity(bookResponse));
    }

    @Transactional(readOnly = true)
    public Book findBookByIdOrThrow(Long bookId) {
        return bookRepository.findById(bookId)
            .orElseThrow(() -> BookNotFoundException.EXCEPTION);
    }
}
