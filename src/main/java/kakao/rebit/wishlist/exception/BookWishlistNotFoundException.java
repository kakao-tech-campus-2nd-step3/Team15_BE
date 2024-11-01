package kakao.rebit.wishlist.exception;

import kakao.rebit.common.exception.BusinessException;

public class BookWishlistNotFoundException extends BusinessException {

    public static final BookWishlistNotFoundException EXCEPTION = new BookWishlistNotFoundException();

    private BookWishlistNotFoundException() {
        super(WishlistErrorCode.WISHLIST_NOT_FOUND);
    }
}
