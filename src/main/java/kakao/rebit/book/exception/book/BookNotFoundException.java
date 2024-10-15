package kakao.rebit.book.exception.book;

import kakao.rebit.common.exception.BusinessException;

public class BookNotFoundException extends BusinessException {

    public static final BusinessException EXCEPTION = new BookNotFoundException();

    private BookNotFoundException() {
        super(BookErrorCode.NOT_FOUND);
    }
}
