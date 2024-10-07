package kakao.rebit.feed.exception.feed;

import kakao.rebit.common.exception.BusinessException;

public class FavoriteBookRequiredBookException extends BusinessException {

    public static final BusinessException EXCEPTION = new FavoriteBookRequiredBookException();

    private FavoriteBookRequiredBookException() {
        super(FeedErrorCode.FAVORITE_BOOK_REQUIRED_BOOK);
    }

}
