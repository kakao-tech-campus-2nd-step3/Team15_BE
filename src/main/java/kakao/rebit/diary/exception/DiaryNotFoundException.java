package kakao.rebit.diary.exception;

import kakao.rebit.common.exception.BusinessException;

public class DiaryNotFoundException extends BusinessException {

    public static final DiaryNotFoundException EXCEPTION = new DiaryNotFoundException();

    private DiaryNotFoundException() {
        super(DiaryErrorCode.DIARY_NOT_FOUND);
    }
}
