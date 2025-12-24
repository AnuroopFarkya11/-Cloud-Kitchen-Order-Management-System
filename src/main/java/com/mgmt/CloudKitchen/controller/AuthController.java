package com.mgmt.CloudKitchen.controller;

import com.mgmt.CloudKitchen.config.JwtUtil;
import com.mgmt.CloudKitchen.dto.response.JwtResponse;
import com.mgmt.CloudKitchen.entity.AppUser;
import com.mgmt.CloudKitchen.entity.RefreshToken;
import com.mgmt.CloudKitchen.handler.CustomException;
import com.mgmt.CloudKitchen.service.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/auth/login")
    public ResponseEntity<Object> login(@RequestBody Map<String, String> map) {
        log.info("Login invoked");
        if (!map.isEmpty()) {
            String username = map.get("username");
            String password = map.get("password");

            if (username != null && password != null) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
                try {
                    authentication = authenticationManager.authenticate(authentication);
                    String token = jwtUtil.generateToken(authentication);
                    AppUser user = (AppUser) authentication.getPrincipal();
                    assert user != null;
                    RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

                    JwtResponse jwtResponse = new JwtResponse(
                            token,
                            refreshToken.getToken(),
                            user.getId(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getAuthorities().toString()
                    );
                    return ResponseEntity.ok(jwtResponse);
                } catch (AuthenticationException authenticationException) {
                    log.error(authenticationException.getLocalizedMessage(), authenticationException);
                    throw new CustomException("Invalid Credentials", HttpStatus.UNAUTHORIZED);
                }
            }

        }
        throw new CustomException("Invalid Credentials", HttpStatus.UNAUTHORIZED);
    }


}
