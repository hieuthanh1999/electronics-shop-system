package com.doantotnghiep.nhanambooks.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final CustomLogin customLogin;
    private final CustomLogout customLogout;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                                .requestMatchers("/love-list/**").authenticated()
                                .anyRequest().permitAll()
                )
                .authenticationProvider(authenticationProvider)
                .formLogin().loginPage("/login").usernameParameter("username").passwordParameter("password").successHandler(customLogin).permitAll()
                .and()

                .logout().logoutUrl("/logout").logoutSuccessHandler(customLogout).permitAll()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
        ;
        return http.build();
    }


}