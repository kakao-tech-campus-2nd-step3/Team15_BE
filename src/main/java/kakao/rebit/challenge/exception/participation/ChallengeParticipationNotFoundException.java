package kakao.rebit.challenge.exception.participation;

import kakao.rebit.common.exception.BusinessException;

public class ChallengeParticipationNotFoundException extends BusinessException {

    public static final BusinessException EXCEPTION = new ChallengeParticipationNotFoundException();

    private ChallengeParticipationNotFoundException() {
        super(ChallengeParticipationErrorCode.CHALLENGE_PARTICIPATION_NOT_FOUND);
    }
}
