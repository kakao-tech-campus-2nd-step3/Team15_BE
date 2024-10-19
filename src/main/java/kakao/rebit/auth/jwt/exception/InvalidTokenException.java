package kakao.rebit.auth.jwt.exception;

import kakao.rebit.common.exception.BusinessException;

public class InvalidTokenException extends BusinessException {

    public static final BusinessException EXCEPTION = new InvalidTokenException();

    private InvalidTokenException() {
        super(JwtErrorCode.INVALID_TOKEN);
    }
}
