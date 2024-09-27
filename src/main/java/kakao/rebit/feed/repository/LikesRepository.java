package kakao.rebit.feed.repository;

import kakao.rebit.feed.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {

}
