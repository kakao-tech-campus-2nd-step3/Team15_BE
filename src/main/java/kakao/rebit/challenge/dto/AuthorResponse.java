package kakao.rebit.challenge.dto;

public record AuthorResponse(
        Long id,
        String nickname,
        String imageKey,
        String presignedUrl
) {

}
