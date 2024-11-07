package kakao.rebit.auth.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.core.MethodParameter;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public JwtArgumentResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(String.class) && parameter.hasParameterAnnotation(AuthTokenParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {
        HttpServletRequest request = ((ServletWebRequest) webRequest).getRequest();
        String token = jwtTokenProvider.extractToken(request.getHeader(HttpHeaders.AUTHORIZATION));
        jwtTokenProvider.validateToken(token);
        return token;
    }
}
