package kakao.rebit.challenge.exception.verification;

import kakao.rebit.common.exception.BusinessException;

public class VerifyChallengeAlreadyDoneTodayException extends BusinessException {

    public static final BusinessException EXCEPTION = new VerifyChallengeAlreadyDoneTodayException();

    private VerifyChallengeAlreadyDoneTodayException() {
        super(VerificationErrorCode.VERIFY_CHALLENGE_ALREADY_DONE_TODAY);
    }
}
