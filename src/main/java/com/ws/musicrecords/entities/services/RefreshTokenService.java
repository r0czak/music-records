package com.ws.musicrecords.entities.services;

import com.ws.musicrecords.entities.RefreshTokenEntity;

import java.util.Optional;

public interface RefreshTokenService {
    Optional<RefreshTokenEntity> findByToken(String token);

    RefreshTokenEntity createRefreshToken(Long userId);

    RefreshTokenEntity verifyExpiration(RefreshTokenEntity token);
}
