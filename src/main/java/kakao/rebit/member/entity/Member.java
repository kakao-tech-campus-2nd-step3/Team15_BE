package kakao.rebit.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kakao.rebit.common.domain.ImageKeyModifier;
import kakao.rebit.common.persistence.BaseEntity;
import kakao.rebit.member.exception.NotEnoughPointsException;

@Entity
@Table(name = "member")
public class Member extends BaseEntity implements ImageKeyModifier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private String imageKey;

    private String bio;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Integer points;

    private String kakaoToken;

    protected Member() {
    }

    public Member(String nickname, String imageKey, String bio, String email, Role role,
            Integer points, String kakaoToken) {
        this.nickname = nickname;
        this.imageKey = imageKey;
        this.bio = bio;
        this.email = email;
        this.role = role;
        this.points = points;
        this.kakaoToken = kakaoToken;
    }

    public static Member init(String nickname, String imageKey, String email, String accessToken) {
        return new Member(nickname, imageKey, "", email, Role.ROLE_USER, 0, accessToken);
    }

    public void updateProfile(String nickname, String bio) {
        this.nickname = nickname;
        this.bio = bio;
    }

    public void addPoints(Integer pointsToAdd) {
        this.points += pointsToAdd;
    }

    public void usePoints(Integer pointsToUse) {
        if (this.points < pointsToUse) {
            throw NotEnoughPointsException.EXCEPTION;
        }
        this.points -= pointsToUse;
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public String getImageKey() {
        return imageKey;
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

    @Override
    public void changeImageKey(String imageKey){
        this.imageKey = imageKey;
    }

}
