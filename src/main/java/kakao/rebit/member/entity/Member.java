package kakao.rebit.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kakao.rebit.common.persistence.BaseEntity;
import kakao.rebit.member.exception.MemberNotEnoughPointsException;

@Entity
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private String imageUrl;

    private String bio;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Integer points;

    private String kakaoToken;

    protected Member() {
    }

    public Member(String nickname, String imageUrl, String bio, String email, Role role,
            Integer points, String kakaoToken) {
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.bio = bio;
        this.email = email;
        this.role = role;
        this.points = points;
        this.kakaoToken = kakaoToken;
    }

    public void updateProfile(String nickname, String bio, String imageUrl) {
        this.nickname = nickname;
        this.bio = bio;
        this.imageUrl = imageUrl;
    }

    public void addPoints(Integer pointsToAdd) {
        this.points += pointsToAdd;
    }

    public void usePoints(Integer pointsToUse) {
        if (this.points < pointsToUse) {
            throw MemberNotEnoughPointsException.EXCEPTION;
        }
        this.points -= pointsToUse;
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

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public Integer getPoints() {
        return points;
    }

    public String getKakaoToken() {
        return kakaoToken;
    }

}
