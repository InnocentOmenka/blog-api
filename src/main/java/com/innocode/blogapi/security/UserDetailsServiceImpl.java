package com.innocode.blogapi.security;

import com.innocode.blogapi.model.entity.User;
import com.innocode.blogapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Loading user by email: " + email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("User not found with email: " + email);
                    return new UsernameNotFoundException("User not found with email: " + email);
                });
        System.out.println("User found: " + user.getEmail());
        return new CustomUserDetails(user);
    }
}
