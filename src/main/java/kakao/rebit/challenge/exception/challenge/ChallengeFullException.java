package kakao.rebit.challenge.exception.challenge;

import kakao.rebit.common.exception.BusinessException;

public class ChallengeFullException extends BusinessException {

    public static final BusinessException EXCEPTION = new ChallengeFullException();

    private ChallengeFullException() {
        super(ChallengeErrorCode.CHALLENGE_FULL);
    }
}
