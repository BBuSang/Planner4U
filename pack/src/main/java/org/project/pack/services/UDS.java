package org.project.pack.services;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;

import org.project.pack.classes.UD;
import org.project.pack.entity.User;
import org.project.pack.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UDS extends DefaultOAuth2UserService implements UserDetailsService {
    
    @Autowired
    UserRepository userRep;
    
    @Value("${auth.user}")
    String userAuth;
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String name = "";
        String pwd = "";
        String email = "";
        Map<String, Object> attributes = user.getAttributes();
        Map<String, Object> response = null;
        
        if (provider.equals("google")) {
            pwd = (attributes.get("sub") != null ? attributes.get("sub").toString() : "unknown");
            name = (attributes.get("name") != null ? attributes.get("name").toString() : "unknown");
            email = (attributes.get("email") != null ? attributes.get("email").toString() : "unknown");
        } else if (provider.equals("kakao")) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");
            email = (kakaoAccount != null && kakaoAccount.get("email") != null ? kakaoAccount.get("email").toString() : "unknown");
            name = (kakaoProfile.get("nickname") != null ? kakaoProfile.get("nickname").toString() : "unknown");
            pwd = (attributes.get("id") != null ? attributes.get("id").toString() : "unknown");
        } else if (provider.equals("naver")) {
            response = (Map<String, Object>) attributes.get("response");
            email = (response != null && response.get("email") != null ? response.get("email").toString() : "unknown");
            name = (response.get("name") != null ? response.get("name").toString() : "unknown");
            pwd = (response != null && response.get("id") != null ? response.get("id").toString() : "unknown");
        }

        User userByName = userRep.findByEmail(email);
        if (userByName == null) {
            userByName = new User(null, name, pwd, provider, email, List.of(userAuth), attributes);
            userRep.save(userByName);
        } else {
            // 필요시 추가 로직
            userByName.getAuths().size();
        }
//        System.out.println(user.getAttributes()); // 사용자 정보 확인용 프린트
        return new UD(userByName);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRep.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }
        // UD 객체에 attributes를 올바르게 설정
        return new UD(user);
    }


    
    public String getUserNameByEmail(String email) {
        User user = userRep.findByEmail(email);
        if (user != null) {
            return user.getName(); 
        } else {
            return null; 
        }
    }
 // 이메일 중복 확인 메서드
    public boolean isEmailExists(String email) {
        return userRep.existsByEmail(email);
    }
}




















