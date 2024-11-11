package kakao.rebit.config;

import kakao.rebit.auth.service.KakaoAuthService;
import kakao.rebit.auth.fixture.TestKakaoAuthService;
import kakao.rebit.auth.fixture.TestS3Service;
import kakao.rebit.auth.token.AuthTokenGenerator;
import kakao.rebit.member.repository.MemberRepository;
import kakao.rebit.s3.service.S3Service;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestConfig {

    @Bean
    @Primary
    public KakaoAuthService kakaoAuthService(MemberRepository memberRepository, AuthTokenGenerator authTokenGenerator) {
        return new TestKakaoAuthService(memberRepository, authTokenGenerator);
    }

    @Bean
    public S3Service s3Service() {
        return new TestS3Service();
    }
}
