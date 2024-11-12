package kakao.rebit.feed.repository;

import kakao.rebit.feed.entity.Magazine;
import kakao.rebit.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MagazineRepository extends JpaRepository<Magazine, Long> {

    Page<Magazine> findAllByMember(Member member, Pageable pageable);
}
