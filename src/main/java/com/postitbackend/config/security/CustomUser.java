package com.postitbackend.config.security;

import com.postitbackend.member.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class CustomUser implements UserDetails {

    private MemberDTO memberDTO;

    public CustomUser(MemberDTO memberDTO) {
        this.memberDTO = memberDTO;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<UserAuth> authList = memberDTO.getAuthList();

        return authList.stream()
                .map((auth) -> new SimpleGrantedAuthority(auth.getAuth()))
                .toList();
    }

    @Override
    public String getPassword() {
        return memberDTO.getPassword();
    }

    @Override
    public String getUsername() {
        return memberDTO.getEmail();
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
        return memberDTO.getEnable() != 0;
    }
}
