package com.example.backend.service;

import com.example.backend.model.ParentEntity;
import com.example.backend.repository.ParentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ParentService implements UserDetailsService {
    @Autowired
    private ParentRepository parentRepository;

    public ParentEntity signup(ParentEntity parent) {
        return parentRepository.save(parent);
    }

    public ParentEntity getByCredentials(final String email, final String password, final PasswordEncoder passwordEncoder) {
        final ParentEntity originalParent = parentRepository.findByEmail(email);
        if(originalParent != null && passwordEncoder.matches(password, originalParent.getPassword())) {
            return originalParent;
        }
        return null;
    }

    //public Parent login(String email) {
    //}

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return parentRepository.findOptionalByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(email));
    }
}
