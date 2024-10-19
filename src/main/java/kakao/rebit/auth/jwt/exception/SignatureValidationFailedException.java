package kakao.rebit.auth.jwt.exception;

import kakao.rebit.common.exception.BusinessException;

public class SignatureValidationFailedException extends BusinessException {

    public static final BusinessException EXCEPTION = new SignatureValidationFailedException();

    private SignatureValidationFailedException() {
        super(JwtErrorCode.SIGNATURE_VALIDATION_FAILED);
    }
}
