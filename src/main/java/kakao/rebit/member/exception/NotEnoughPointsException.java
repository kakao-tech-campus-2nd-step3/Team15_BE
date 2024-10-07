package kakao.rebit.member.exception;

import kakao.rebit.common.exception.BusinessException;

public class NotEnoughPointsException extends BusinessException {

    public static final BusinessException EXCEPTION = new NotEnoughPointsException();

    private NotEnoughPointsException() {
        super(MemberErrorCode.NOT_ENOUGH_POINTS);
    }
}
