package kakao.rebit.challenge.exception.participation;

import kakao.rebit.common.exception.BusinessException;

public class ChallengeParticipationNotParticipantException extends BusinessException {

    public static final BusinessException EXCEPTION = new ChallengeParticipationNotParticipantException();

    private ChallengeParticipationNotParticipantException() {
        super(ChallengeParticipationErrorCode.CHALLENGE_PARTICIPATION_NOT_PARTICIPANT);
    }
}
