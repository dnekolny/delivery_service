package com.example.delivery_service.services;

import com.example.delivery_service.model.User;
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

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByName(userName);

        optionalUser.orElseThrow(() -> new UsernameNotFoundException("Username not found!!!"));

        return optionalUser.map(UserDetailsImpl::new).get();
    }
}
