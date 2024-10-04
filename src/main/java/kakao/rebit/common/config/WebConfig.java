package kakao.rebit.common.config;

import kakao.rebit.auth.jwt.JwtInterceptor;
import kakao.rebit.member.resolver.MemberInfoArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;
    private final MemberInfoArgumentResolver memberInfoArgumentResolver;

    @Autowired
    public WebConfig(JwtInterceptor jwtInterceptor, MemberInfoArgumentResolver memberInfoArgumentResolver) {
        this.jwtInterceptor = jwtInterceptor;
        this.memberInfoArgumentResolver = memberInfoArgumentResolver;
    }

    @Bean
    public RestClient restClient() {
        return RestClient.builder().build();
    }

    // 인터셉터 등록
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
            .addPathPatterns("/api/members/**")
            .excludePathPatterns("/api/auth/**");
    }

    // Argument Resolver 등록
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberInfoArgumentResolver);
    }
}
