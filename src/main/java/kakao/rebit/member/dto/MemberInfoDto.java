package kakao.rebit.member.dto;

public class MemberInfoDto {

    private final String email;
    private final String role;

    public MemberInfoDto(String email, String role) {
        this.email = email;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
