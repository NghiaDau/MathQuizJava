package org.example.mathquiz.Entities;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import jakarta.persistence.Column;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User,UserDetails   {

    private String AvatarUrl;
    private OAuth2User oauth2User;

    private User user;
    public CustomOAuth2User(OAuth2User oauth2User,User user) {
        this.oauth2User = oauth2User;
        this.user = user;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    public String getId(){
        return user.getId();
    }
    public String disPlayAvatar(){
        return user.getAvatarUrl();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2User.getAuthorities();

    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return oauth2User.getAttribute("name");
    }


    @Override
    public String getName() {
        return oauth2User.getAttribute("name");
    }

    public String getEmail() {
        return oauth2User.<String>getAttribute("email");
    }



}
