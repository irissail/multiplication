package com.gosia.multiplication.service;

import com.gosia.multiplication.DTO.UserDTO;
import com.gosia.multiplication.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Getter
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private UserDTO currentUser;


    @Transactional
    public void registerUser(UserDTO userDTO) throws Exception {
        if (userExist(userDTO.getEmail())) {
            throw new Exception("Użytkownik o takim adresie e-mail już istnieje: " + userDTO.getEmail());
        }
        userDTO.setRoles(Collections.singleton("USER"));
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(userDTO);
    }

    private boolean userExist(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        currentUser = userRepository.findByEmail(email);
        return currentUser;
    }



}
