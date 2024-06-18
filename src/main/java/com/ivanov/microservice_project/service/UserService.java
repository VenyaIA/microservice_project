package com.ivanov.microservice_project.service;

import com.ivanov.microservice_project.dto.UserDTO;

import com.ivanov.microservice_project.entity.User;
import com.ivanov.microservice_project.entity.enums.EnumRole;
import com.ivanov.microservice_project.exception.UserExistException;
import com.ivanov.microservice_project.payload.request.SignupRequest;
import com.ivanov.microservice_project.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class UserService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(SignupRequest customerIn) {
        User user = new User();
        user.setEmail(customerIn.getEmail());
        user.setFirstname(customerIn.getFirstname());
        user.setLastname(customerIn.getLastname());
        user.setUsername(customerIn.getUsername());
        user.setPosition(customerIn.getPosition());
        user.setHourlyRate(customerIn.getHourlyRate());
        user.setPhoneNumber(customerIn.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(customerIn.getPassword()));
        user.getRoles().add(EnumRole.ROLE_USER);
        try {
            LOG.info("Saving customer {}", user.getEmail());
            return userRepository.save(user);
        } catch (Exception e) {
            LOG.error("Error during registration. {}", e.getMessage());
            throw new UserExistException("The customer " + user.getUsername() + " already exist. Please check credentials");
        }
    }

    public User getCurrentUser(Principal principal) {
        return getUserByPrincipal(principal);
    }

    public User updateUser(UserDTO userDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setPhoneNumber(userDTO.getPhoneNumber());

        return userRepository.save(user);
    }

    public User getUserById(Long customerId) {
        return userRepository.findById(customerId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findCustomerByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username: " + username));
    }

}

