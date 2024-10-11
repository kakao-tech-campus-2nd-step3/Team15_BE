package kakao.rebit.book.exception.api;

import kakao.rebit.common.exception.BusinessException;

public class ApiErrorException extends BusinessException {

    public static final BusinessException EXCEPTION = new ApiErrorException();

    private ApiErrorException() {
        super(ApiErrorCode.API_ERROR);
    }
}
