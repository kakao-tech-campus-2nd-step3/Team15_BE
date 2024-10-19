package kakao.rebit.member.resolver;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import kakao.rebit.auth.jwt.JwtTokenProvider;
import kakao.rebit.auth.jwt.exception.AccessDeniedException;
import kakao.rebit.member.annotation.MemberInfo;
import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.entity.Role;
import kakao.rebit.member.service.MemberService;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class MemberInfoArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberInfoArgumentResolver(MemberService memberService,
            JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(MemberInfo.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        String token = jwtTokenProvider.extractToken(request.getHeader(AUTHORIZATION_HEADER));

        String email = jwtTokenProvider.getEmailFromToken(token);
        Member member = memberService.findMemberByEmailOrThrow(email);

        // 예외처리추가
        checkMemberRole(member, parameter);

        return toMemberResponse(member);
    }

    private void checkMemberRole(Member member, MethodParameter parameter) {
        MemberInfo memberInfo = parameter.getParameterAnnotation(MemberInfo.class);
        if (memberInfo != null) {
            Role[] allowedRoles = memberInfo.allowedRoles();
            if (!List.of(allowedRoles).contains(member.getRole())) {
                throw AccessDeniedException.EXCEPTION;
            }
        }
    }

    private MemberResponse toMemberResponse(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getNickname(),
                member.getImageKey(),
                member.getBio(),
                member.getEmail(),
                member.getRole(),
                member.getPoints()
        );
    }
}
