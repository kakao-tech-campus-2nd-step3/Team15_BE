package kakao.rebit.challenge.dto;

import jakarta.validation.constraints.Positive;

public record ChallengeParticipationRequest(
        @Positive(message = "입장료는 1 이상이어야 합니다.")
        Integer entryFee
) {

}
