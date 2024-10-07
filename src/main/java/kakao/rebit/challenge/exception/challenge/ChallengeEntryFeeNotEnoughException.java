package kakao.rebit.challenge.exception.challenge;

import kakao.rebit.common.exception.BusinessException;

public class ChallengeEntryFeeNotEnoughException extends BusinessException {

    public static final BusinessException EXCEPTION = new ChallengeEntryFeeNotEnoughException();

    private ChallengeEntryFeeNotEnoughException() {
        super(ChallengeErrorCode.CHALLENGE_ENTRY_FEE_NOT_ENOUGH);
    }
}
