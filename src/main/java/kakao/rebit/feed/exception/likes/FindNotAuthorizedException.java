package kakao.rebit.feed.exception.likes;

import kakao.rebit.common.exception.BusinessException;

public class FindNotAuthorizedException extends BusinessException {

    public static final BusinessException EXCEPTION = new FindNotAuthorizedException();

    private FindNotAuthorizedException() {
        super(LikesErrorCode.FIND_NOT_AUTHORIZED);
    }

}
