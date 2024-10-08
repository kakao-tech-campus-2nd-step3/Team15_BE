package kakao.rebit.book.exception.book;

import kakao.rebit.common.exception.BusinessException;

public class InvalidIsbnException extends BusinessException {
    public static final BusinessException EXCEPTION = new InvalidIsbnException();

    private InvalidIsbnException() {
        super(BookErrorCode.INVALID_ISBN);
    }
}
