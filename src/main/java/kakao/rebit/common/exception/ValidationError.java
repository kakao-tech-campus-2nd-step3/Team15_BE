package kakao.rebit.common.exception;

import org.springframework.validation.FieldError;

public record ValidationError(
        String field,
        String message
) {
    public static ValidationError of(final FieldError fieldError) {
        return new ValidationError(fieldError.getField(), fieldError.getDefaultMessage());
    }
}
