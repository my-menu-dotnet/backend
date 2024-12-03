package net.mymenu.service;

import net.mymenu.models.User;
import net.mymenu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User loadUserByUsername(String email) {
        return loadUserByEmail(email);
    }

    public User loadUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElse(null);
    }
}
