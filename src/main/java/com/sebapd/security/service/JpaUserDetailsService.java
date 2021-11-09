package com.sebapd.security.service;

import com.sebapd.security.config.CustomUserDetails;
import com.sebapd.security.entity.User;
import com.sebapd.security.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

       User user = userRepository.findUserByUsername(username)
               .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

       return new CustomUserDetails(user);
    }

    public void addUser(User user){
       userRepository.save(user);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }
}
