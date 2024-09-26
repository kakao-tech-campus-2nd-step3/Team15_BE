package kakao.rebit.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kakao.rebit.common.persistence.BaseEntity;

@Entity
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private String imageUrl;

    private String bio;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Integer point;

    private String kakaoToken;

    protected Member() {
    }

    public Member(String nickname, String imageUrl, String bio, Role role, Integer point, String kakaoToken) {
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.bio = bio;
        this.role = role;
        this.point = point;
        this.kakaoToken = kakaoToken;
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
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
