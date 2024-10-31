package kakao.rebit.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import kakao.rebit.common.config.JpaAuditingConfig;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DataJpaTest(properties = {"spring.jpa.properties.hibernate.jdbc.time_zone=Asia/Seoul"})
@Import(JpaAuditingConfig.class)
public @interface RepositoryTest {

}
