package kakao.rebit.challenge.exception.challenge;

import kakao.rebit.common.exception.BusinessException;

public class InvalidChallengeStartDateException extends BusinessException {

    public static final BusinessException EXCEPTION = new InvalidChallengeStartDateException();

    private InvalidChallengeStartDateException() {
        super(ChallengeErrorCode.INVALID_CHALLENGE_START_DATE);
    }
}
