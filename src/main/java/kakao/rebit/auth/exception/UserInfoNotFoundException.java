package kakao.rebit.auth.exception;

import kakao.rebit.common.exception.BusinessException;

public class UserInfoNotFoundException extends BusinessException {
    public static final UserInfoNotFoundException EXCEPTION = new UserInfoNotFoundException();

    private UserInfoNotFoundException() {
        super(AuthErrorCode.USER_INFO_NOT_FOUND);
    }
}
