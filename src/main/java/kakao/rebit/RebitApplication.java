package kakao.rebit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class RebitApplication {

    public static void main(String[] args) {
        SpringApplication.run(RebitApplication.class, args);
    }

}
