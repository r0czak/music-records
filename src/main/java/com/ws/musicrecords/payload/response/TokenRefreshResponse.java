package com.ws.musicrecords.payload.response;

import lombok.Data;
import org.springframework.lang.NonNull;

@Data
public class TokenRefreshResponse {
    @NonNull
    private String accessToken;
    @NonNull
    private String refreshToken;
    private String tokenType = "Bearer";
}
