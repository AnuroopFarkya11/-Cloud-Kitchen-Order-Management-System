package com.mgmt.CloudKitchen.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String refreshToken;
    private Integer userId;
    private String username;
    private String email;
    private String roles;
}
