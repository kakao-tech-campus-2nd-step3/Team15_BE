package kakao.rebit.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import kakao.rebit.common.domain.ImageKeyAccessor;

public record MemberRequest(

        @NotBlank(message = "닉네임은 필수입니다.")
        String nickname,

        String bio,

        @NotBlank(message = "이미지는 필수입니다.")
        @Pattern(regexp = "^member" + ImageKeyAccessor.BASE_IMAGE_KEY_FORMAT + "|^member/default_image$", message = "멤버 imageKey는 'member/UUID/filename' 형식이어야 합니다.")
        String imageKey,

        @NotBlank(message = "이미지는 필수입니다.")
        @Pattern(regexp = "^cover" + ImageKeyAccessor.BASE_IMAGE_KEY_FORMAT + "|^cover/default_image$", message = "커버 imageKey는 'cover/UUID/filename' 형식이어야 합니다.")
        String coverImageKey
) {

}
