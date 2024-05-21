package org.example.config.oauth;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Parent;
import org.example.repository.ParentRepository;
import org.example.service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserOAuth2Service extends DefaultOAuth2UserService {
    @Autowired
    private ParentRepository parentRepository;
    private Parent parentInfo;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        Map<String, Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");
        String email = (String) kakao_account.get("email");

        if (email == null) {
            email = "kakao_email";
        }

        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        String nickname = (String) properties.get("nickname");

        Parent parent = parentRepository.findOptionalByEmail(email)
                .map(entity -> entity.update(nickname))
                .orElse(Parent.builder()
                        .email(email)
                        .nickname(nickname)
                        .build());
        parentInfo = parentRepository.save(parent);

        return oAuth2User;
    }

    public String getParentNickname() {
        return parentInfo.getNickname();
    }
}
