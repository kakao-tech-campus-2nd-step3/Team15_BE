package kakao.rebit.diary.repository;

import java.util.List;
import java.util.Optional;
import kakao.rebit.diary.entity.Diary;
import kakao.rebit.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    @Query("SELECT d FROM Diary d WHERE d.member.id = :memberId AND YEAR(d.date) = :year AND MONTH(d.date) = :month")
    List<Diary> findByMemberIdAndYearAndMonth(@Param("memberId") Long memberId, @Param("year") int year, @Param("month") int month);

    Optional<Diary> findByIdAndMemberId(Long id, Long memberId);

    long countByMember(Member member);
}
