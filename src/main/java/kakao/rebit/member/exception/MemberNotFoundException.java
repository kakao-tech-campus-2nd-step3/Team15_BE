package kakao.rebit.member.exception;

import kakao.rebit.common.exception.BusinessException;

public class MemberNotFoundException extends BusinessException {
    public static final MemberNotFoundException EXCEPTION = new MemberNotFoundException();

    private MemberNotFoundException() {
        super(MemberErrorCode.MEMBER_NOT_FOUND);
    }
}
