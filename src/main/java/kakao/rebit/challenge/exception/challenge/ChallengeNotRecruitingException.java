package kakao.rebit.challenge.exception.challenge;

import kakao.rebit.common.exception.BusinessException;

public class ChallengeNotRecruitingException extends BusinessException {

    public static final BusinessException EXCEPTION = new ChallengeNotRecruitingException();

    private ChallengeNotRecruitingException() {
        super(ChallengeErrorCode.CHALLENGE_NOT_RECRUITING);
    }
}
