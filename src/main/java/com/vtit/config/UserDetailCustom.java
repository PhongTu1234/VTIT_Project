package com.vtit.config;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.vtit.entity.Users;
import com.vtit.entity.Roles;
import com.vtit.service.UserService;

@Component("userDetailsService")
public class UserDetailCustom implements UserDetailsService {

    private final UserService userService;

    public UserDetailCustom(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = this.userService.handleGetUserByUsernamea(username);

        if (user == null) {
            throw new BadCredentialsException("Thông tin đăng nhập sai");
        }

        // Copy ra Set mới để tránh ConcurrentModification
        Set<SimpleGrantedAuthority> authorities = user.getRole()
                .getPermissions()
                .stream()
                .map(p -> new SimpleGrantedAuthority(p.getCode()))
                .collect(Collectors.toSet());

        return new User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }

}
