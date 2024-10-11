package kakao.rebit.challenge.exception.challenge;

import kakao.rebit.common.exception.BusinessException;

public class NotRecruitingException extends BusinessException {

    public static final BusinessException EXCEPTION = new NotRecruitingException();

    private NotRecruitingException() {
        super(ChallengeErrorCode.NOT_RECRUITING);
    }
}
