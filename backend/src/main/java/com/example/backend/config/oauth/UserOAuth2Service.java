package com.example.backend.config.oauth;

import com.example.backend.model.ParentEntity;
import com.example.backend.repository.ParentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserOAuth2Service extends DefaultOAuth2UserService {
    @Autowired
    private ParentRepository parentRepository;
    private ParentEntity parentInfo;

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

        ParentEntity parent = parentRepository.findOptionalByEmail(email)
                .map(entity -> entity.update(nickname))
                .orElse(ParentEntity.builder()
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

