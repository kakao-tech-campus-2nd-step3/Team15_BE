package kakao.rebit.challenge.exception.challenge;

import kakao.rebit.common.exception.BusinessException;

public class ChallengeDeleteNotAllowedException extends BusinessException {

    public static final BusinessException EXCEPTION = new ChallengeDeleteNotAllowedException();

    private ChallengeDeleteNotAllowedException() {
        super(ChallengeErrorCode.CHALLENGE_DELETE_NOT_ALLOWED);
    }
}
