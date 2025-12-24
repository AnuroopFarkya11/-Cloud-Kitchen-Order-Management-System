package com.mgmt.CloudKitchen.service;

import com.mgmt.CloudKitchen.entity.AppUser;
import com.mgmt.CloudKitchen.entity.RefreshToken;
import com.mgmt.CloudKitchen.handler.CustomException;
import com.mgmt.CloudKitchen.repo.RefreshTokenRepository;
import com.mgmt.CloudKitchen.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {


    @Value("${app.jwtRefreshExpirationMs:604800000}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;


    @Autowired
    private UserRepository userRepository;


    public RefreshToken findByToken(String token) {
        return refreshTokenRepository
                .findByToken(token).orElseThrow(() -> new CustomException("No Refresh token", HttpStatus.NOT_FOUND));
    }

    public RefreshToken createRefreshToken(Integer userId) {
        AppUser user = userRepository.findById(userId).orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(user);
        String token = UUID.randomUUID().toString();
        refreshToken.setToken(token);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));

        return refreshToken;

    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new CustomException("RefreshToken is expired. Please make the new sign in request", HttpStatus.BAD_REQUEST);
        }
        return token;
    }

    /**
     * Deletes the refresh token (used during logout).
     */
    public int deleteByUserId(Integer userId)
    {
        AppUser user = userRepository.findById(userId).orElseThrow(()->new CustomException("User not found",HttpStatus.NOT_FOUND));
        return refreshTokenRepository.deleteByUser(user);
    }


}
