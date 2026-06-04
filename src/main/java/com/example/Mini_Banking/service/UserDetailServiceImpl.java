package com.example.Mini_Banking.service;

import com.example.Mini_Banking.entity.AppUser;
import com.example.Mini_Banking.repository.AppUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Tại sao lại phải implement UserDetailsService?
 * -> Để Spring biết cách load user. Đây là cách thống nhát để Spring biết load user từ đâu
 *
 * Tại sao return Spring User?
 * -> Vì Spring Security authenticate bằng UserDetails (của Spring), không phải entity của bạn.
 * Tại sao truyền email/password/authorities?
 * -> Vì đó là 3 thứ Spring Security cần để: xác định user, check password, check role/quyền
 *
 */

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    public final AppUserRepository appUserRepository;

    UserDetailServiceImpl(AppUserRepository appUserRepository){
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername (String email) {
        AppUser user = appUserRepository.findUserByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new User(
                user.getEmail(),
                user.getPasswordHash(),
                List.of(new SimpleGrantedAuthority(user.getRole()))
        );
    }
}