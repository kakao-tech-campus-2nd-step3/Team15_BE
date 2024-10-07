package kakao.rebit.challenge.exception.challenge;

import kakao.rebit.common.exception.BusinessException;

public class ChallengeDeleteNotAuthorizedException extends BusinessException {

    public static final BusinessException EXCEPTION = new ChallengeDeleteNotAuthorizedException();

    private ChallengeDeleteNotAuthorizedException() {
        super(ChallengeErrorCode.CHALLENGE_DELETE_NOT_AUTHORIZED);
    }
}
