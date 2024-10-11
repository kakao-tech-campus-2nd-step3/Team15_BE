package kakao.rebit.challenge.exception.challenge;

import kakao.rebit.common.exception.BusinessException;

public class InvalidChallengePeriodException extends BusinessException {

    public static final BusinessException EXCEPTION = new InvalidChallengePeriodException();

    private InvalidChallengePeriodException() {
        super(ChallengeErrorCode.INVALID_CHALLENGE_PERIOD);
    }
}
