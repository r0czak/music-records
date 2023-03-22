package com.ws.musicrecords.controllers;

import com.ws.musicrecords.entities.RefreshTokenEntity;
import com.ws.musicrecords.entities.RoleEntity;
import com.ws.musicrecords.entities.UserEntity;
import com.ws.musicrecords.entities.enums.ERole;
import com.ws.musicrecords.entities.services.impl.RefreshTokenServiceImpl;
import com.ws.musicrecords.exception.TokenRefreshException;
import com.ws.musicrecords.payload.request.LoginRequest;
import com.ws.musicrecords.payload.request.SignupRequest;
import com.ws.musicrecords.payload.request.TokenRefreshRequest;
import com.ws.musicrecords.payload.response.JwtResponse;
import com.ws.musicrecords.payload.response.MessageResponse;
import com.ws.musicrecords.payload.response.TokenRefreshResponse;
import com.ws.musicrecords.repository.RoleRepository;
import com.ws.musicrecords.repository.UserRepository;
import com.ws.musicrecords.security.jwt.JwtUtils;
import com.ws.musicrecords.security.services.impl.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired AuthenticationManager authenticationManager;

    @Autowired UserRepository userRepository;

    @Autowired RoleRepository roleRepository;

    @Autowired PasswordEncoder encoder;

    @Autowired JwtUtils jwtUtils;

    @Autowired
    RefreshTokenServiceImpl refreshTokenService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication =
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles =
            userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(
            new JwtResponse(
                jwt,
                refreshToken.getToken(),
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Błąd: Nazwa użytkownika jest już zajęta"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Błąd: Email jest już zajęty"));
        }

        // Create new user's account
        UserEntity user =
            new UserEntity(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<RoleEntity> roles = new HashSet<>();

        if (strRoles == null) {
            RoleEntity userRole =
                roleRepository
                    .findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Rola nie istnieje"));
            roles.add(userRole);
        } else {
            strRoles.forEach(
                role -> {
                    switch (role) {
                        case "admin":
                            RoleEntity adminRole =
                                roleRepository
                                    .findByName(ERole.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error: Rola nie istnieje"));
                            roles.add(adminRole);

                            break;
                        default:
                            RoleEntity userRole =
                                roleRepository
                                    .findByName(ERole.ROLE_USER)
                                    .orElseThrow(() -> new RuntimeException("Error: Rola nie istnieje"));
                            roles.add(userRole);
                    }
                });
        }
        user.setRoles(roles);

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Użytkownik zarejestrowany pomyślnie"));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService
            .findByToken(requestRefreshToken)
            .map(refreshTokenService::verifyExpiration)
            .map(RefreshTokenEntity::getUser)
            .map(
                user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
            .orElseThrow(
                () ->
                    new TokenRefreshException(
                        requestRefreshToken, "Refresh tokena nie ma w bazie"));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        UserDetailsImpl userDetails =
            (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        refreshTokenService.deleteByUserId(userId);
        return ResponseEntity.ok(new MessageResponse("Wylogowanie pomyślne"));
    }
}

