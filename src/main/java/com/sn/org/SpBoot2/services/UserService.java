package com.sn.org.SpBoot2.services;

import com.sn.org.SpBoot2.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;



@Service
public class UserService implements AuthenticationProvider {
    @Autowired
    UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails userDetails=userRepository.getByEmail(authentication.getName());
        if (userDetails==null)
            throw new BadCredentialsException("Вы не зарегистрированы!");
        if (!authentication.getCredentials().toString().equals(userDetails.getPassword()))
            throw new BadCredentialsException("Неверный Пароль!");
        return new UsernamePasswordAuthenticationToken(
                userDetails,userDetails.getPassword(),userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}