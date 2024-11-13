package kakao.rebit.member.exception;

import kakao.rebit.common.exception.BusinessException;

public class MemberNotFoundException extends BusinessException {

    public static final BusinessException EXCEPTION = new MemberNotFoundException();

    private MemberNotFoundException() {
        super(MemberErrorCode.NOT_FOUND);
    }
}
