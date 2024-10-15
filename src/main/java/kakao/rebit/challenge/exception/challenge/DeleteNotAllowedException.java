package kakao.rebit.challenge.exception.challenge;

import kakao.rebit.common.exception.BusinessException;

public class DeleteNotAllowedException extends BusinessException {

    public static final BusinessException EXCEPTION = new DeleteNotAllowedException();

    private DeleteNotAllowedException() {
        super(ChallengeErrorCode.DELETE_NOT_ALLOWED);
    }
}
