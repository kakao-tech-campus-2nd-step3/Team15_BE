package kakao.rebit.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

public record ErrorResponse(
        String code,
        String status,
        String message,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<ValidationError> invalidParams
) {

    public ErrorResponse(ErrorCode errorCode) {
        this(errorCode, null);
    }

    public ErrorResponse(ErrorCode errorCode, List<ValidationError> invalidParams) {
        this(errorCode.getCode(), errorCode.getHttpStatusCodeAndName(), errorCode.getMessage(), invalidParams);
    }
}
