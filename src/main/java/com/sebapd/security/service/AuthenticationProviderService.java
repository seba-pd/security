package com.sebapd.security.service;

import com.sebapd.security.config.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationProviderService implements AuthenticationProvider {

    private final JpaUserDetailsService userDetailsService;
    private  BCryptPasswordEncoder bCryptPasswordEncoder;
    private  SCryptPasswordEncoder sCryptPasswordEncoder;

    public AuthenticationProviderService(JpaUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        var username = authentication.getName();
        var password = authentication.getCredentials().toString();

        var user = userDetailsService.loadUserByUsername(username);

        switch(user.getUser().getEncryptionAlgorithm()){
            case BCRYPT:
                return checkPassword(user,password,bCryptPasswordEncoder);
            case SCRYPT:
                return checkPassword(user,password,sCryptPasswordEncoder);
        }
        throw new BadCredentialsException("Bad credentials");
    }

    private Authentication checkPassword(CustomUserDetails user,
                                         String rawPassword,
                                         PasswordEncoder encoder){
        if (encoder.matches(rawPassword, user.getPassword())){
            return new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    user.getPassword(),
                    user.getAuthorities());
        } else
                throw new BadCredentialsException("Bad credentials");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class
                .isAssignableFrom(aClass);
    }

    @Autowired
    public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Autowired
    public void setsCryptPasswordEncoder(SCryptPasswordEncoder sCryptPasswordEncoder) {
        this.sCryptPasswordEncoder = sCryptPasswordEncoder;
    }
}
