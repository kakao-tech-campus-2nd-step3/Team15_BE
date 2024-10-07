package kakao.rebit.feed.exception.likes;

import kakao.rebit.common.exception.BusinessException;

public class LikesNotPressedException extends BusinessException {

    public static final BusinessException EXCEPTION = new LikesNotPressedException();

    private LikesNotPressedException() {
        super(LikesErrorCode.LIKES_NOT_PRESSED);
    }

}
