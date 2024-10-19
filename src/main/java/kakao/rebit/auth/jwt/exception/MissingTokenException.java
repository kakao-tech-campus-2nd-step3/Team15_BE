package kakao.rebit.auth.jwt.exception;

import kakao.rebit.common.exception.BusinessException;

public class MissingTokenException extends BusinessException {

    public static final BusinessException EXCEPTION = new MissingTokenException();

    private MissingTokenException(){
        super(JwtErrorCode.MISSING_TOKEN);
    }

}
