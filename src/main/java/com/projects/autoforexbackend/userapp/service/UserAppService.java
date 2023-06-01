package com.projects.autoforexbackend.userapp.service;

import com.projects.autoforexbackend.userapp.model.UserApp;
import com.projects.autoforexbackend.userapp.repository.UserAppRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserAppService implements UserDetailsService {

    private UserAppRepository userAppRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userAppRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("email %s tidak ditemukan", email)));
    }

    public String signUp(UserApp userApp) throws IllegalAccessException {
        boolean existsUser = userAppRepository.findByEmail(userApp.getEmail()).isPresent();
        if (existsUser) {
            throw new IllegalAccessException("email sudah dipakai");
        }

        String encode = bCryptPasswordEncoder.encode(userApp.getPassword());
        userApp.setPassword(encode);
        userAppRepository.save(userApp);
        return "works";
    }
}
