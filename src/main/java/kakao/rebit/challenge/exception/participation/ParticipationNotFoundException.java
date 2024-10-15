package kakao.rebit.challenge.exception.participation;

import kakao.rebit.common.exception.BusinessException;

public class ParticipationNotFoundException extends BusinessException {

    public static final BusinessException EXCEPTION = new ParticipationNotFoundException();

    private ParticipationNotFoundException() {
        super(ParticipationErrorCode.NOT_FOUND);
    }
}
