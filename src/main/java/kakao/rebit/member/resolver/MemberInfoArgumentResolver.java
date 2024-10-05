package kakao.rebit.member.resolver;

import jakarta.servlet.http.HttpServletRequest;
import kakao.rebit.member.annotation.MemberInfo;
import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class MemberInfoArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;

    @Autowired
    public MemberInfoArgumentResolver(MemberService memberService) {
        this.memberService = memberService;
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
        String email = (String) request.getAttribute("email");
        String role = (String) request.getAttribute("role");

        if (email == null || role == null) {
            throw new IllegalStateException("JWT 토큰이 유효하지 않거나 누락되었습니다.");
        }

        // 데이터베이스에서 회원 정보 조회 후 MemberResponse로 반환
        Member member = memberService.findMemberByEmailOrThrow(email);
        return toMemberResponse(member);
    }

    private MemberResponse toMemberResponse(Member member) {
        return new MemberResponse(
            member.getId(),
            member.getNickname(),
            member.getImageUrl(),
            member.getBio(),
            member.getEmail(),
            member.getRole(),
            member.getPoint()
        );
    }
}
