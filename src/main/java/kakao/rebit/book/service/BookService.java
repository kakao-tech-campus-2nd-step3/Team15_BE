package kakao.rebit.book.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import kakao.rebit.book.dto.AladinApiResponseListResponse;
import kakao.rebit.book.dto.AladinApiResponseResponse;
import kakao.rebit.book.dto.BookDetailResponse;
import kakao.rebit.book.dto.BookResponse;
import kakao.rebit.book.entity.Book;
import kakao.rebit.book.exception.book.BookNotFoundException;
import kakao.rebit.book.mapper.BookMapper;
import kakao.rebit.book.repository.BookRepository;
import kakao.rebit.feed.entity.FavoriteBook;
import kakao.rebit.feed.repository.FavoriteBookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AladinApiService aladinApiService;
    private final FavoriteBookRepository favoriteBookRepository;

    public BookService(BookRepository bookRepository, AladinApiService aladinApiService,
        FavoriteBookRepository favoriteBookRepository) {
        this.bookRepository = bookRepository;
        this.aladinApiService = aladinApiService;
        this.favoriteBookRepository = favoriteBookRepository;
    }

    @Transactional(readOnly = true)
    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll().stream()
            .map(BookMapper::toBookResponse)
            .collect(Collectors.toList());
    }

    // 책 타이틀로 검색 후 저장 및 반환
    @Transactional
    public List<BookResponse> searchAndSaveBooksByTitle(String title) {
        AladinApiResponseListResponse bookList = aladinApiService.searchBooksByTitle(title);

        // 검색된 책 목록을 처리하여 이미 저장된 책은 조회하고, 새 책은 저장
        List<Book> foundBooks = bookList.item().stream()
            .map(book -> bookRepository.findByIsbn(book.isbn())
                .orElseGet(() -> bookRepository.save(BookMapper.toBookEntity(book))))
            .toList();

        return foundBooks.stream()
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

    // 새로운 한줄평과 서평을 가져오는 메서드
    @Transactional
    public BookDetailResponse getBookDetailReview(String isbn) {
        Book book = searchAndSaveBookByIsbn(isbn);
        Optional<FavoriteBook> topFavoriteBook = favoriteBookRepository.findTopByBookOrderByLikesDesc(
            book);
        return BookMapper.toBookDetailResponse(book, topFavoriteBook.orElse(null));
    }

    @Transactional(readOnly = true)
    public List<String> getBriefReviewsByIsbn(String isbn) {
        return favoriteBookRepository.findAllByBookIsbnOrderByLikesDesc(isbn)
            .stream()
            .map(FavoriteBook::getBriefReview)
            .collect(Collectors.toList());
    }
}
