package kakao.rebit.auth.jwt.exception;

import kakao.rebit.common.exception.BusinessException;

public class UnsupportedTokenException extends BusinessException {

    public static final BusinessException EXCEPTION = new UnsupportedTokenException();

    private UnsupportedTokenException(){
        super(JwtErrorCode.UNSUPPORTED_TOKEN);
    }

}
