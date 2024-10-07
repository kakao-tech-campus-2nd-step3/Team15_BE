package kakao.rebit.auth.controller;

import kakao.rebit.auth.dto.LoginResponse;
import kakao.rebit.auth.service.KakaoAuthService;
import kakao.rebit.common.annotation.AllowAnonymous;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/auth")
public class KakaoAuthController {

    @Value("${OAUTH_KAKAO_CLIENT_ID}")
    private String clientId;

    @Value("${OAUTH_KAKAO_REDIRECT_URI}")
    private String redirectUri;

    private final KakaoAuthService kakaoAuthService;

    public KakaoAuthController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    // 테스트용 html
    // 로그인 버튼을 클릭하면 인가코드를 받아옵니다.
    @AllowAnonymous
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("clientId", clientId);
        model.addAttribute("redirectUri", redirectUri);
        return "loginForm";
    }

    @AllowAnonymous
    @GetMapping("/login/oauth/kakao")
    @ResponseBody
    public LoginResponse kakaoLogin(@RequestParam("code") String code) {

        return kakaoAuthService.kakaoLogin(code);
    }
}
