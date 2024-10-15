package kakao.rebit.challenge.exception.verification;

import kakao.rebit.common.exception.BusinessException;

public class VerificationNotFoundException extends BusinessException {

    public static final BusinessException EXCEPTION = new VerificationNotFoundException();

    private VerificationNotFoundException() {
        super(VerificationErrorCode.VERIFICATION_NOT_FOUND);
    }
}
