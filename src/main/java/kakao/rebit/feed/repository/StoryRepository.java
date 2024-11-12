package kakao.rebit.feed.repository;

import kakao.rebit.feed.entity.Story;
import kakao.rebit.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository extends JpaRepository<Story, Long> {

    Page<Story> findAllByMember(Member member, Pageable pageable);
}
