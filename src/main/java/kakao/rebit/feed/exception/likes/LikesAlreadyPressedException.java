package kakao.rebit.feed.exception.likes;

import kakao.rebit.common.exception.BusinessException;

public class LikesAlreadyPressedException extends BusinessException {

    public static final BusinessException EXCEPTION = new LikesAlreadyPressedException();

    private LikesAlreadyPressedException() {
        super(LikesErrorCode.LIKES_ALREADY_PRESSED);
    }

}
