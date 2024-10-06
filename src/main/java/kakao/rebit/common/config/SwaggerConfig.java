package kakao.rebit.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.servlet.ServletContext;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI openAPI(ServletContext servletContext) {

        String contextPath = servletContext.getContextPath();
        Server server = new Server().url(contextPath);

        // JWT 토큰을 위한 보안 스킴 정의
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList(SECURITY_SCHEME_NAME);

        return new OpenAPI()
                .servers(List.of(server))
                .info(swaggerInfo())
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, securityScheme))
                .addSecurityItem(securityRequirement);
    }

    private Info swaggerInfo() {
        License license = new License();
        license.setUrl("https://github.com/kakao-tech-campus-2nd-step3/Team15_BE");
        license.setName("Rebit");

        return new Info()
                .version("v0.0.1")
                .title("Rebit API문서")
                .description("Rebit의 API 문서 입니다.")
                .license(license);
    }
}
