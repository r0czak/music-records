package com.ws.musicrecords.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.List;

@Data
public class JwtResponse {
    @NonNull
    private String token;
    private String type = "Bearer";
    @NonNull
    private String refreshToken;
    @NonNull
    private Long id;
    @NonNull
    private String username;
    @NonNull
    private String email;
    @NonNull
    private List<String> roles;
}
