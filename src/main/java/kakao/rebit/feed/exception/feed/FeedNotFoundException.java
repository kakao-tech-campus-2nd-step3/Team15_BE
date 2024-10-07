package kakao.rebit.feed.exception.feed;

import kakao.rebit.common.exception.BusinessException;

public class FeedNotFoundException extends BusinessException {

    public static final BusinessException EXCEPTION = new FeedNotFoundException();

    private FeedNotFoundException() {
        super(FeedErrorCode.NOT_FOUND);
    }

}
