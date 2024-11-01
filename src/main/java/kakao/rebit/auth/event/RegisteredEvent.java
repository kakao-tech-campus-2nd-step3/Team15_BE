package kakao.rebit.auth.event;

public record RegisteredEvent(
        String email,
        String profileImageUrl
) {

    public static RegisteredEvent init(String email, String profileImageUrl) {
        return new RegisteredEvent(email, profileImageUrl);
    }
}
