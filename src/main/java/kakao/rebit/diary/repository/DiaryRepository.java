package kakao.rebit.diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import kakao.rebit.diary.entity.Diary;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
}