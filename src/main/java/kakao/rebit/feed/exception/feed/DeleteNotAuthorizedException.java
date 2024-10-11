package kakao.rebit.feed.exception.feed;

import kakao.rebit.common.exception.BusinessException;

public class DeleteNotAuthorizedException extends BusinessException {

    public static final BusinessException EXCEPTION = new DeleteNotAuthorizedException();

    private DeleteNotAuthorizedException() {
        super(FeedErrorCode.DELETE_NOT_AUTHORIZED);
    }
}
