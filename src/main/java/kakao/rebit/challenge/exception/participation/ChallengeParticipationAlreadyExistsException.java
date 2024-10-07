package kakao.rebit.challenge.exception.participation;

import kakao.rebit.common.exception.BusinessException;

public class ChallengeParticipationAlreadyExistsException extends BusinessException {

    public static final BusinessException EXCEPTION = new ChallengeParticipationAlreadyExistsException();

    private ChallengeParticipationAlreadyExistsException() {
        super(ChallengeParticipationErrorCode.CHALLENGE_PARTICIPATION_ALREADY_EXISTS);
    }
}
