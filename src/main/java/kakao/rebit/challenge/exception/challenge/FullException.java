package kakao.rebit.challenge.exception.challenge;

import kakao.rebit.common.exception.BusinessException;

public class FullException extends BusinessException {

    public static final BusinessException EXCEPTION = new FullException();

    private FullException() {
        super(ChallengeErrorCode.FULL);
    }
}
