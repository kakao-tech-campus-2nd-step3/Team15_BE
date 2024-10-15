package kakao.rebit.challenge.exception.verification;

import kakao.rebit.common.exception.BusinessException;

public class DeleteNotAuthorizedException extends BusinessException {

    public static final BusinessException EXCEPTION = new DeleteNotAuthorizedException();

    private DeleteNotAuthorizedException() {
        super(VerificationErrorCode.DELETE_NOT_AUTHORIZED);
    }
}
