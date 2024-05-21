package org.example.config.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.entity.Parent;
import org.example.security.TokenProvider;
import org.example.service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private ParentService parentService;
    @Autowired
    private TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        Map<String, Object> kakao_account = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
        String email = (String) kakao_account.get("email");
        if (email == null) {
            email = "kakao_email";
        }
        Map<String, Object> properties = (Map<String, Object>) oAuth2User.getAttributes().get("properties");
        String nickname = (String) properties.get("nickname");

        //Parent parent = parentService.login(email);

        //HttpSession session = request.getSession();
        //session.setAttribute("parent", parent);
        String jwt = tokenProvider.createForOAuth("kakao", email, nickname); // email을 안 줘서 이 부분은 소용이 없음. email을 랜덤으로 생성한다 해도 다음에 다시 로그인할 때는 그 랜덤 email을 찾지 못할 것임. 닉네임은 중복이어도 되고... 이메일을 받아올 수 있도록 해야할 것 같음. 그러려면 비즈니스 앱으로 전환해야 됨... 어쩔 수 없이 닉네임을 중복이 아닌 방향으로 가야할듯
        String url = makeRedirectUrl(jwt);

        getRedirectStrategy().sendRedirect(request, response, url);
    }

    private String makeRedirectUrl(String token) {
        return UriComponentsBuilder.fromUriString("http://localhost:3000/oauth2/redirect/"+ token)
                .build().toUriString();
    }
}