package kakao.rebit.feed.exception.feed;

import kakao.rebit.common.exception.BusinessException;

public class UpdateNotAuthorizedException extends BusinessException {

    public static final BusinessException EXCEPTION = new UpdateNotAuthorizedException();

    public UpdateNotAuthorizedException() {
        super(FeedErrorCode.UPDATE_NOT_AUTHORIZED);
    }
}
