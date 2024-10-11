package kakao.rebit.challenge.exception.challenge;

import kakao.rebit.common.exception.BusinessException;

public class EntryFeeNotEnoughException extends BusinessException {

    public static final BusinessException EXCEPTION = new EntryFeeNotEnoughException();

    private EntryFeeNotEnoughException() {
        super(ChallengeErrorCode.ENTRY_FEE_NOT_ENOUGH);
    }
}
