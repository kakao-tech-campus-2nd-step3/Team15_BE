package kakao.rebit.member.dto;

import jakarta.validation.constraints.NotBlank;

public record AdminMemberRequest(
        @NotBlank(message = "닉네임은 필수입니다.")
        String nickname,

        String bio,

        Integer point
) {

}
