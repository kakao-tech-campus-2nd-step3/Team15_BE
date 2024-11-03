package kakao.rebit.feed.exception.feed;

import kakao.rebit.common.exception.BusinessException;

public class InvalidFeedFormatException extends BusinessException {

    public static final BusinessException EXCEPTION = new InvalidFeedFormatException();

    private InvalidFeedFormatException() {
        super(FeedErrorCode.INVALID_FEED_FORMAT);
    }
}
