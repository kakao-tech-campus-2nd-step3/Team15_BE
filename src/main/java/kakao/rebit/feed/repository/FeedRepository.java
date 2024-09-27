package kakao.rebit.feed.repository;

import kakao.rebit.feed.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long> {

}
