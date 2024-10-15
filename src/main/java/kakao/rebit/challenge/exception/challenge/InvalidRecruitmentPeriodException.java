package kakao.rebit.challenge.exception.challenge;

import kakao.rebit.common.exception.BusinessException;

public class InvalidRecruitmentPeriodException extends BusinessException {

    public static final BusinessException EXCEPTION = new InvalidRecruitmentPeriodException();

    private InvalidRecruitmentPeriodException() {
        super(ChallengeErrorCode.INVALID_RECRUITMENT_PERIOD);
    }
}
