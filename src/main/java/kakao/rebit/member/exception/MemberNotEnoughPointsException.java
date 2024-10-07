package kakao.rebit.member.exception;

import kakao.rebit.common.exception.BusinessException;

public class MemberNotEnoughPointsException extends BusinessException {

    public static final BusinessException EXCEPTION = new MemberNotEnoughPointsException();

    private MemberNotEnoughPointsException() {
        super(MemberErrorCode.MEMBER_NOT_ENOUGH_POINTS);
    }
}
