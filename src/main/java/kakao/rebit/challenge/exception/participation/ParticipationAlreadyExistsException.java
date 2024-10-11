package kakao.rebit.challenge.exception.participation;

import kakao.rebit.common.exception.BusinessException;

public class ParticipationAlreadyExistsException extends BusinessException {

    public static final BusinessException EXCEPTION = new ParticipationAlreadyExistsException();

    private ParticipationAlreadyExistsException() {
        super(ParticipationErrorCode.ALREADY_EXISTS);
    }
}
