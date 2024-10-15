package kakao.rebit.challenge.exception.challenge;

import kakao.rebit.common.exception.BusinessException;

public class DeleteNotAuthorizedException extends BusinessException {

    public static final BusinessException EXCEPTION = new DeleteNotAuthorizedException();

    private DeleteNotAuthorizedException() {
        super(ChallengeErrorCode.DELETE_NOT_AUTHORIZED);
    }
}
