package kakao.rebit.challenge.exception.challenge;

import kakao.rebit.common.exception.BusinessException;

public class InvalidHeadcountRangeException extends BusinessException {

    public static final BusinessException EXCEPTION = new InvalidHeadcountRangeException();

    private InvalidHeadcountRangeException() {
        super(ChallengeErrorCode.INVALID_HEADCOUNT_RANGE);
    }
}
