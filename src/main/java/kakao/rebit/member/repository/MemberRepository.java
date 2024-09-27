package kakao.rebit.member.repository;

import java.util.Optional;
import kakao.rebit.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByKakaoToken(String kakaoToken);

}
