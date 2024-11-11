package kakao.rebit.wishlist.exception;

import kakao.rebit.common.exception.BusinessException;

public class AlreadyInWishlistException extends BusinessException {

    public static final AlreadyInWishlistException EXCEPTION = new AlreadyInWishlistException();

    private AlreadyInWishlistException() {
        super(WishlistErrorCode.ALREADY_IN_WISHLIST);
    }
}
