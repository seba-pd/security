package com.sebapd.security.api;

import com.sebapd.security.config.CustomUserDetails;
import com.sebapd.security.entity.User;
import com.sebapd.security.service.JpaUserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final JpaUserDetailsService userDetailsService;

    public UserController(JpaUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("{username}")
    public ResponseEntity<CustomUserDetails> getUser(@PathVariable String username){
        return new ResponseEntity<>(userDetailsService.loadUserByUsername(username), HttpStatus.OK);
    }

    @PostMapping("/add_user")
    public void addUser(@RequestBody User user){
       userDetailsService.addUser(user);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id){
        userDetailsService.deleteUser(id);
    }


}
