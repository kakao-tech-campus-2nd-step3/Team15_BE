package kakao.rebit.book.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import kakao.rebit.book.dto.AladinApiResponseListResponse;
import kakao.rebit.book.entity.Book;
import kakao.rebit.book.repository.BookRepository;
import kakao.rebit.feed.dto.response.BookResponse;
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
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
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




}
