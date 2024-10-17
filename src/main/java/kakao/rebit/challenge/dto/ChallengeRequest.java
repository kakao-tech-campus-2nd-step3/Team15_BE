package kakao.rebit.challenge.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import kakao.rebit.challenge.entity.ChallengeType;
import kakao.rebit.challenge.exception.challenge.InvalidChallengePeriodException;
import kakao.rebit.challenge.exception.challenge.InvalidChallengeStartDateException;
import kakao.rebit.challenge.exception.challenge.InvalidHeadcountRangeException;
import kakao.rebit.challenge.exception.challenge.InvalidRecruitmentPeriodException;
import kakao.rebit.common.domain.ImageKeyAccessor;

public record ChallengeRequest (
        @NotBlank(message = "제목은 필수입니다.")
        @Size(min = 2, max = 100, message = "제목은 2자 이상 100자 이하여야 합니다.")
        String title,

        @NotBlank(message = "내용은 필수입니다.")
        @Size(max = 1000, message = "내용은 1000자 이하여야 합니다.")
        String content,

        @NotBlank(message = "이미지는 필수입니다.")
        @Pattern(regexp = "^challenge" + ImageKeyAccessor.BASE_IMAGE_KEY_FORMAT, message = "챌린지 imageKey는 'challenge/%s/%s' 형식이어야 합니다.")
        String imageKey,

        @NotNull(message = "챌린지 유형은 필수입니다.")
        ChallengeType type,

        @NotNull(message = "최소 참가비는 필수입니다.")
        @Positive(message = "최소 참가비는 1 이상이어야 합니다.")
        Integer minimumEntryFee,

        @NotNull(message = "모집 시작일은 필수입니다.")
        @Future(message = "모집 시작일은 현재보다 미래여야 합니다.")
        LocalDateTime recruitmentStartDate,

        @NotNull(message = "모집 종료일은 필수입니다.")
        @Future(message = "모집 종료일은 현재보다 미래여야 합니다.")
        LocalDateTime recruitmentEndDate,

        @NotNull(message = "챌린지 시작일은 필수입니다.")
        @Future(message = "챌린지 시작일은 현재보다 미래여야 합니다.")
        LocalDateTime challengeStartDate,

        @NotNull(message = "챌린지 종료일은 필수입니다.")
        @Future(message = "챌린지 종료일은 현재보다 미래여야 합니다.")
        LocalDateTime challengeEndDate,

        @NotNull(message = "최소 참가자 수는 필수입니다.")
        @Positive(message = "최소 참가자 수는 1명 이상이어야 합니다.")
        Integer minHeadcount,

        @NotNull(message = "최대 참가자 수는 필수입니다.")
        @Positive(message = "최대 참가자 수는 1명 이상이어야 합니다.")
        Integer maxHeadcount
) {

    public void validate() {
        if (recruitmentStartDate.isAfter(recruitmentEndDate)) {
            throw InvalidRecruitmentPeriodException.EXCEPTION;
        }

        if (challengeStartDate.isAfter(challengeEndDate)) {
            throw InvalidChallengePeriodException.EXCEPTION;
        }

        if (challengeStartDate.isBefore(recruitmentEndDate)) {
            throw InvalidChallengeStartDateException.EXCEPTION;
        }

        if (minHeadcount > maxHeadcount) {
            throw InvalidHeadcountRangeException.EXCEPTION;
        }
    }
}
