package kakao.rebit.feed.repository;

import kakao.rebit.feed.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository extends JpaRepository<Story, Long> {

}
