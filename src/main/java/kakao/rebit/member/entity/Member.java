package kakao.rebit.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kakao.rebit.common.persistence.BaseEntity;

@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String imageUrl;

    private String bio;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Integer point;

    private String kakaoToken;

    protected Member() {
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getBio() {
        return bio;
    }

    public Role getRole() {
        return role;
    }

    public Integer getPoint() {
        return point;
    }

    public String getKakaoToken() {
        return kakaoToken;
    }
}
