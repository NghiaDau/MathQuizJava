package org.example.mathquiz.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User implements UserDetails {

    private static final long serialVersionUID = 1113799434508676095L;


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    private String userName;

    private String passwordHash;
    private String email;
    private String phoneNumber;
    private String avatarUrl;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    private String fullName;
    private int countFail;
    private Date lockExpired;
    private boolean enabled;
    private String resetPasswordToken;
    private Date resetPasswordTokenExpired;

    @Column(name = "provider", length = 50)
    private String provider;

    @ManyToOne
    @JsonBackReference(value = "grade-user")
    @JoinColumn(name = "grade_id")
    private Grade grade;

    @JsonManagedReference(value = "user-exam")
    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    private List<Exam> exams;

    @JsonManagedReference(value = "user-result")
    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    private List<Result> results;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> userRoles = this.getRoles();
        return userRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .toList();
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    public String disPlayAvatar() {
        return avatarUrl;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean isStatus(){
        return enabled;
    }

}
