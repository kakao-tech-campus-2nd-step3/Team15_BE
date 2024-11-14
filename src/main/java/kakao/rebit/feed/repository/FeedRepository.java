package kakao.rebit.feed.repository;

import java.util.Optional;
import kakao.rebit.feed.entity.Feed;
import kakao.rebit.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    Optional<Feed> findByIdAndMember(Long feedId, Member member);

    Page<Feed> findAllByMember(Member member, Pageable pageable);

    long countByMember(Member member);
}
