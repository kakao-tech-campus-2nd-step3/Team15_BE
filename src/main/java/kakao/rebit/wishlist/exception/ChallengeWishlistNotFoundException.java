package kakao.rebit.wishlist.exception;

import kakao.rebit.common.exception.BusinessException;

public class ChallengeWishlistNotFoundException extends BusinessException {

    public static final ChallengeWishlistNotFoundException EXCEPTION = new ChallengeWishlistNotFoundException();

    private ChallengeWishlistNotFoundException() {
        super(WishlistErrorCode.WISHLIST_CHALLENGE_NOT_FOUND);
    }
}
