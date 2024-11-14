package kakao.rebit.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import kakao.rebit.book.dto.AladinApiResponseListResponse;
import kakao.rebit.book.dto.BookResponse;
import kakao.rebit.book.entity.Book;
import kakao.rebit.book.exception.book.BookNotFoundException;
import kakao.rebit.book.fixture.BookFixture;
import kakao.rebit.book.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AladinApiService aladinApiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 전체책_목록_조회_책_응답_리스트_반환() {
        // given
        List<Book> books = List.of(
                BookFixture.createBookWithIsbn("isbn1"),
                BookFixture.createBookWithIsbn("isbn2"),
                BookFixture.createBookWithIsbn("isbn3")
        );
        when(bookRepository.findAll()).thenReturn(books);

        // when
        List<BookResponse> bookResponses = bookService.getAllBooks();

        // then
        assertThat(bookResponses).hasSize(books.size());
        assertThatList(bookResponses).extracting(BookResponse::isbn)
            .containsExactly("isbn1", "isbn2", "isbn3");
    }

    @Test
    void 책_제목_검색_페이지네이션된_책_목록_반환() {
        // given
        String title = "Test Title";
        PageRequest pageable = PageRequest.of(0, 10);
        when(aladinApiService.searchBooksByTitle(title, pageable))
                .thenReturn(new AladinApiResponseListResponse(List.of()));

        // when
        Page<BookResponse> books = bookService.searchAndSaveBooksByTitle(title, pageable);

        // then
        assertThat(books).isNotNull();
        verify(aladinApiService, times(1)).searchBooksByTitle(title, pageable);
    }

    @Test
    void 책_상세정보_조회_책존재시_책_응답_반환() {
        // given
        String isbn = "테스트 isbn";
        Book book = BookFixture.createDefault();
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));

        // when
        BookResponse bookResponse = bookService.getBookDetail(isbn);

        // then
        assertThat(bookResponse.isbn()).isEqualTo(isbn);
        verify(bookRepository, times(1)).findByIsbn(isbn);
    }

    @Test
    void 책_상세정보_조회_책_존재하지않을때_예외발생() {
        // given
        String isbn = "non-existent-isbn";
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());
        when(aladinApiService.searchBookByIsbn(isbn)).thenThrow(BookNotFoundException.EXCEPTION);

        // when, then
        assertThatThrownBy(() -> bookService.getBookDetail(isbn))
                .isInstanceOf(BookNotFoundException.class);
        verify(bookRepository, times(1)).findByIsbn(isbn);
    }
}
