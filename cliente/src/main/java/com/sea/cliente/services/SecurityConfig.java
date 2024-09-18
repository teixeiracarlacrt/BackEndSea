package com.sea.cliente.services;

import lombok.var;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/clientes/novo", "/clientes/{id}/editar", "/clientes/{id}/atualizar", "/clientes/{id}/deletar").hasRole("ADMIN")
                .antMatchers("/clientes/**").authenticated()
                .and()
                .formLogin()
                .defaultSuccessUrl("/clientes", true)
                .and()
                .logout()
                .permitAll()
                .and()
                .csrf().disable();
        return http.build();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        var userDetailsManager = new InMemoryUserDetailsManager();

        userDetailsManager.createUser(
                User.withDefaultPasswordEncoder()
                        .username("admin")
                        .password("123qwe!@#")
                        .roles("ADMIN")
                        .build()
        );

        userDetailsManager.createUser(
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("123qwe123")
                        .roles("USER")
                        .build()
        );

        return userDetailsManager;
    }
}
