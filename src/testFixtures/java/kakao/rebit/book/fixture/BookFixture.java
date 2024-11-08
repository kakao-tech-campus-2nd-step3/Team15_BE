package kakao.rebit.book.fixture;

import kakao.rebit.book.entity.Book;
import kakao.rebit.book.fixture.values.BookDefaultValues;

public class BookFixture {

    public static Book createDefault(){
        BookDefaultValues defaults = BookDefaultValues.INSTANCE;
        return new Book(
                defaults.isbn(),
                defaults.title(),
                defaults.description(),
                defaults.author(),
                defaults.publisher(),
                defaults.cover(),
                defaults.pubDate()
        );
    }

    public static Book createUpdateBook(){
        BookDefaultValues defaults = BookDefaultValues.INSTANCE;
        return new Book(
                "업데이트 isbn",
                "업데이트 제목",
                defaults.description(),
                defaults.author(),
                defaults.publisher(),
                defaults.cover(),
                defaults.pubDate()
        );
    }
}
