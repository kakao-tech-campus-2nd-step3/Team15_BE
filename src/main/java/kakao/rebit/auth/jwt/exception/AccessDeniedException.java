package kakao.rebit.auth.jwt.exception;

import kakao.rebit.common.exception.BusinessException;

public class AccessDeniedException extends BusinessException {

    public static final BusinessException EXCEPTION = new AccessDeniedException();

    private AccessDeniedException() {
        super(JwtErrorCode.ACCESS_DENIED);
    }
}
