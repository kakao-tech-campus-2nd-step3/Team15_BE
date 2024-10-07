package kakao.rebit.challenge.exception.challenge;

import kakao.rebit.common.exception.BusinessException;

public class ChallengeNotFoundException extends BusinessException {

    public static final BusinessException EXCEPTION = new ChallengeNotFoundException();

    private ChallengeNotFoundException() {
        super(ChallengeErrorCode.CHALLENGE_NOT_FOUND);
    }
}
