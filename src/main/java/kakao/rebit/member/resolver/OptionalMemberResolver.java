package kakao.rebit.member.resolver;

import jakarta.servlet.http.HttpServletRequest;
import kakao.rebit.auth.jwt.JwtTokenProvider;
import kakao.rebit.member.annotation.MemberInfoIfPresent;
import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.mapper.MemberMapper;
import kakao.rebit.member.service.MemberService;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class OptionalMemberResolver implements HandlerMethodArgumentResolver {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    JwtTokenProvider jwtTokenProvider;
    MemberService memberService;
    MemberMapper memberMapper;

    public OptionalMemberResolver(JwtTokenProvider jwtTokenProvider, MemberService memberService, MemberMapper memberMapper) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
        this.memberMapper = memberMapper;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(MemberInfoIfPresent.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        String headerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (headerToken == null) {
            return null;
        }

        String token = jwtTokenProvider.extractToken(headerToken);

        String email = jwtTokenProvider.getEmailFromToken(token);
        Member member = memberService.findMemberByEmailOrThrow(email);

        return memberMapper.toMemberResponse(member);

    }
}
