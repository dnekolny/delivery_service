package com.example.delivery_service.services;

import com.example.delivery_service.model.Entity.User;
import com.example.delivery_service.model.UserDetailsImpl;
import com.example.delivery_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isPresent() && !optionalUser.get().isActive()){
            throw new UsernameNotFoundException("User access denied!");
        }

        optionalUser.orElseThrow(() -> new UsernameNotFoundException("Username not found!!!"));

        return optionalUser.map(UserDetailsImpl::new).get();
    }
}
