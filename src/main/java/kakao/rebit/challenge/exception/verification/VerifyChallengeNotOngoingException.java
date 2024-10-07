package kakao.rebit.challenge.exception.verification;

import kakao.rebit.common.exception.BusinessException;

public class VerifyChallengeNotOngoingException extends BusinessException {

    public static final BusinessException EXCEPTION = new VerifyChallengeNotOngoingException();

    private VerifyChallengeNotOngoingException() {
        super(VerificationErrorCode.VERIFY_CHALLENGE_NOT_ON_GOING);
    }
}
