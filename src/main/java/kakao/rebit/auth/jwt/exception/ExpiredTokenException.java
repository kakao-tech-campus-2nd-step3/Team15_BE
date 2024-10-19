package kakao.rebit.auth.jwt.exception;

import kakao.rebit.common.exception.BusinessException;

public class ExpiredTokenException extends BusinessException {

    public static final BusinessException EXCEPTION = new ExpiredTokenException();

    private ExpiredTokenException() {
        super(JwtErrorCode.EXPIRED_TOKEN);
    }
}
