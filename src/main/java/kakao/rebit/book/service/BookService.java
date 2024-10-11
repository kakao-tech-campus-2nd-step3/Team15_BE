package kakao.rebit.book.service;

import java.util.List;
import java.util.Optional;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    @Transactional
    public Page<BookResponse> searchAndSaveBooksByTitle(String title, Pageable pageable) {
        AladinApiResponseListResponse bookList = aladinApiService.searchBooksByTitle(title,
            pageable);

        List<Book> foundBooks = bookList.item().stream()
            .map(book -> bookRepository.findByIsbn(book.isbn())
                .orElseGet(() -> bookRepository.save(BookMapper.toBookEntity(book))))
            .toList();

        List<BookResponse> bookResponses = foundBooks.stream()
            .map(BookMapper::toBookResponse)
            .collect(Collectors.toList());

        return new PageImpl<>(bookResponses, pageable, bookList.item().size());
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
