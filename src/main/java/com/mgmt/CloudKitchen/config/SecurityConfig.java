package com.mgmt.CloudKitchen.config;


import com.mgmt.CloudKitchen.entity.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private UserDetailsService userDetailsService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable());
        httpSecurity.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        httpSecurity.authorizeHttpRequests(auth -> auth.requestMatchers(
                "/auth/login",
                "/api/v1/menu/**",
                "/api/v1/orders/*/status",
                "/h2-console/**"
        ).permitAll());


        httpSecurity.authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/orders/**").hasAuthority(RoleType.KITCHEN_STAFF.name()));
//        httpSecurity.authorizeHttpRequests(auth->auth.requestMatchers("/api/v1/kitchen/**").hasAuthority(RoleType.CUSTOMER.name()));
//        httpSecurity.authorizeHttpRequests(auth->auth.requestMatchers("/api/v1/delivery/**").hasAuthority(RoleType.CUSTOMER.name()));

        httpSecurity.sessionManagement(smc -> smc.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.userDetailsService(userDetailsService);
        httpSecurity.formLogin(withDefaults());
        httpSecurity.httpBasic(withDefaults());
        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


        httpSecurity.exceptionHandling(
                exc -> exc.accessDeniedHandler(((request, response, accessDeniedException) -> {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType("application/json");
                    response.getWriter().write(new ObjectMapper().writeValueAsString(Map.of("error", accessDeniedException.getMessage())));
                    response.getWriter().flush();
        })));

        return httpSecurity.build();

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }


}
