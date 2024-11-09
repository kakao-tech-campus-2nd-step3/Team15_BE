package kakao.rebit.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import kakao.rebit.common.domain.ImageKeyAccessor;

public record MemberRequest(
        String nickname,

        String bio,

        @NotBlank(message = "이미지는 필수입니다.")
        @Pattern(regexp = "^member" + ImageKeyAccessor.BASE_IMAGE_KEY_FORMAT + "|^member/default_profile$", message = "멤버 imageKey는 'member/UUID/filename' 형식이어야 합니다.")
        String imageKey,

        Integer point
) {

}
