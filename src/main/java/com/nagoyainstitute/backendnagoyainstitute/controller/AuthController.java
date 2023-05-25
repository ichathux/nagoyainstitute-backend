package com.nagoyainstitute.backendnagoyainstitute.controller;

import com.nagoyainstitute.backendnagoyainstitute.dto.*;
import com.nagoyainstitute.backendnagoyainstitute.service.AuthService;
import com.nagoyainstitute.backendnagoyainstitute.service.RefreshTokenService;
import com.nagoyainstitute.backendnagoyainstitute.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:8080",allowCredentials = "true",allowedHeaders = "*")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    @PostMapping("signup")
    public ResponseEntity<String> signUp(@RequestBody RegisterRequestInit registerRequestInit){
        log.info("sign up request received : "+registerRequestInit);

        authService.signUp(userService.createRegisterRequest(registerRequestInit));
        return new ResponseEntity<>("User Registration Successful", HttpStatus.OK);
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> accountVerification(@PathVariable String token){
        log.debug("starting to verify account : "+token);
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account activated successfully", HttpStatus.OK);
    }
    @PostMapping("login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest){
        log.info("Login request from :"+loginRequest);
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                    loginRequest.getUsername(), loginRequest.getPassword()));
//        } catch (final BadCredentialsException ex) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
//        }
//
//        final UserDetails userDetails = userDetailService.loadUserByUsername(loginRequest.getUsername());
//        return new AuthenticationResponse(loginRequest.getUsername(),tokenService.generateToken(userDetails));

        return authService.login(loginRequest);
    }
    @PostMapping("refresh/token")
    public AuthenticationResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body("Refresh token deleted successfully");
    }
}
