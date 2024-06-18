package com.ivanov.microservice_project.service;

import com.ivanov.microservice_project.entity.User;
import com.ivanov.microservice_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    public UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username: " + username));

        return build(user);
    }

    public User loadUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public static User build(User customer) {
        List<GrantedAuthority> authorities = customer.getRoles().stream()
                .map(enumRole -> new SimpleGrantedAuthority(enumRole.name()))
                .collect(Collectors.toList());
        return new User(
                customer.getId(),
                customer.getUsername(),
                customer.getEmail(),
                customer.getPassword(),
                authorities
        );
    }
}
