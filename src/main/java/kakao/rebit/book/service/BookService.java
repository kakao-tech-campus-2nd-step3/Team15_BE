package kakao.rebit.book.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import kakao.rebit.book.entity.Book;
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
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Transactional
    public String searchAndSaveBooksByTitle(String title) {
        String apiResponse = aladinApiService.searchBookByTitle(title);
        List<Book> books = parseBooks(apiResponse);
        books.stream()
            .filter(book -> bookRepository.findByIsbn(book.getIsbn()).isEmpty())
            .forEach(bookRepository::save);
        return apiResponse;
    }

    @Transactional
    public Book searchAndSaveBookByIsbn(String isbn) {
        String apiResponse = aladinApiService.searchBookByIsbn(isbn);
        Book book = parseBookDetail(apiResponse);
        Book savedBook = bookRepository.findByIsbn(book.getIsbn())
            .orElseGet(() -> {
                return bookRepository.save(book);
            });
        return savedBook;
    }

    @Transactional(readOnly = true)
    public Book getBookDetail(String isbn) {
        return bookRepository.findByIsbn(isbn)
            .orElseGet(() -> searchAndSaveBookByIsbn(isbn));
    }

    @Transactional(readOnly = true)
    public Book findBookByIdOrThrow(Long bookId){
        return bookRepository.findById(bookId).orElseThrow(() -> new IllegalArgumentException("해당 책이 존재하지 않습니다."));
    }

    private List<Book> parseBooks(String apiResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> responseMap = objectMapper.readValue(apiResponse,
                new TypeReference<Map<String, Object>>() {
                });
            return objectMapper.convertValue(responseMap.get("item"),
                new TypeReference<List<Book>>() {
                });
        } catch (IOException e) {
            throw new RuntimeException("API 응답에서 책 데이터를 파싱하는 데 실패했습니다", e);
        }
    }

    private Book parseBookDetail(String apiResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> responseMap = objectMapper.readValue(apiResponse,
                new TypeReference<Map<String, Object>>() {
                });

            // 알라딘  api 에서 책 관련 정보는 'item' 필드 배열로 반환되어 전송 됨
            List<Map<String, Object>> items = (List<Map<String, Object>>) responseMap.get("item");

            if (items == null || items.isEmpty()) {
                throw new RuntimeException("API 응답에 책 정보가 없습니다.");
            }

            Map<String, Object> item = items.get(0);

            String isbn = (String) item.get("isbn");
            String title = (String) item.get("title");
            String description = (String) item.get("description");
            String author = (String) item.get("author");
            String publisher = (String) item.get("publisher");
            String imageUrl = (String) item.get("cover");
            String pubDate = (String) item.get("pubdate");

            return new Book(isbn, title, description, author, publisher, imageUrl,pubDate);

        } catch (IOException e) {
            throw new RuntimeException("API 응답에서 책 상세 정보를 파싱하는 데 실패했습니다", e);
        }
    }
}
