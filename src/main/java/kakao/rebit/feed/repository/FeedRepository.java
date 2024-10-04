package kakao.rebit.feed.repository;

import java.util.Optional;
import kakao.rebit.feed.entity.Feed;
import kakao.rebit.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    Optional<Feed> findByIdAndMember(Long feedId, Member member);
}
