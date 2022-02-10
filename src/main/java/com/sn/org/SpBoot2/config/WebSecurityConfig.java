package com.sn.org.SpBoot2.config;


import com.sn.org.SpBoot2.model.Role;
import com.sn.org.SpBoot2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            
                .authorizeRequests()
                    .antMatchers("/", "/about","/blog/{id}", "/img/**","/user/add").permitAll()
                    .antMatchers("/add","/edit/**").authenticated()

                    .antMatchers("/user/**","/edit/**").hasAuthority(Role.ADMIN.name())
                   // .anyRequest().authenticated()
                .and()
                    .formLogin()
                  //  .loginPage("/login")
                    .permitAll()
                .and()
                    .logout()
                    .logoutSuccessUrl("/")
                    .permitAll()

        ;


    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(userService);
    }
    @Bean
    protected PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

}
