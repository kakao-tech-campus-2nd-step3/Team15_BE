package kakao.rebit.common.config;

import java.time.Duration;
import java.util.List;
import kakao.rebit.auth.jwt.JwtInterceptor;
import kakao.rebit.auth.jwt.JwtArgumentResolver;
import kakao.rebit.member.resolver.MemberInfoArgumentResolver;
import kakao.rebit.member.resolver.OptionalMemberResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestClient;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableScheduling
public class WebConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;
    private final MemberInfoArgumentResolver memberInfoArgumentResolver;
    private final OptionalMemberResolver optionalMemberResolver;
    private final JwtArgumentResolver jwtArgumentResolver;

    @Autowired
    public WebConfig(JwtInterceptor jwtInterceptor,
            MemberInfoArgumentResolver memberInfoArgumentResolver, OptionalMemberResolver optionalMemberResolver,
            JwtArgumentResolver jwtArgumentResolver) {
        this.jwtInterceptor = jwtInterceptor;
        this.memberInfoArgumentResolver = memberInfoArgumentResolver;
        this.optionalMemberResolver = optionalMemberResolver;
        this.jwtArgumentResolver = jwtArgumentResolver;
    }

    @Bean
    public RestClient restClient(RestClient.Builder restClientBuilder) {
        // 타임아웃 설정
        return restClientBuilder
                .requestFactory(ClientHttpRequestFactories.get(
                        ClientHttpRequestFactorySettings.DEFAULTS
                                .withConnectTimeout(Duration.ofSeconds(5))
                                .withReadTimeout(Duration.ofMinutes(2))
                ))
                .build();
    }

    // CORS 설정
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    // 인터셉터 등록
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .excludePathPatterns(
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v3/api-docs/**",
                        "/api/auth/logout"
                );
    }

    // Argument Resolver 등록
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberInfoArgumentResolver);
        resolvers.add(optionalMemberResolver);
        resolvers.add(jwtArgumentResolver);
    }
}
