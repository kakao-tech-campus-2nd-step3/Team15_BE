package kakao.rebit.challenge.exception.participation;

import kakao.rebit.common.exception.BusinessException;

public class ParticipationNotParticipantException extends BusinessException {

    public static final BusinessException EXCEPTION = new ParticipationNotParticipantException();

    private ParticipationNotParticipantException() {
        super(ParticipationErrorCode.NOT_PARTICIPANT);
    }
}
