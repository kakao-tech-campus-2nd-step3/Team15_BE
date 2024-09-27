package kakao.rebit.book.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import kakao.rebit.book.entity.Book;
import kakao.rebit.book.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AladinApiService aladinApiService;

    @Autowired
    public BookService(BookRepository bookRepository, AladinApiService aladinApiService) {
        this.bookRepository = bookRepository;
        this.aladinApiService = aladinApiService;
    }

    public List<Book> searchBooksByTitle(String query) {
        String apiResponse = aladinApiService.searchBookByTitle(query);
        List<Book> books = parseBooks(apiResponse);
        for (Book book : books) {
            bookRepository.findByIsbn(book.getIsbn()).orElseGet(() -> bookRepository.save(book));
        }
        return books;
    }
}
