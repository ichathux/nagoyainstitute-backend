package com.nagoyainstitute.backendnagoyainstitute.service;

import com.nagoyainstitute.backendnagoyainstitute.dto.AuthenticationResponse;
import com.nagoyainstitute.backendnagoyainstitute.dto.LoginRequest;
import com.nagoyainstitute.backendnagoyainstitute.dto.RefreshTokenRequest;
import com.nagoyainstitute.backendnagoyainstitute.dto.RegisterRequest;
import com.nagoyainstitute.backendnagoyainstitute.exception.SpringCustomException;
import com.nagoyainstitute.backendnagoyainstitute.model.User;
import com.nagoyainstitute.backendnagoyainstitute.model.VerifcationToken;
import com.nagoyainstitute.backendnagoyainstitute.repository.UserRepository;
import com.nagoyainstitute.backendnagoyainstitute.repository.VerificationTokenRepository;
import com.nagoyainstitute.backendnagoyainstitute.security.JwtTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    //    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void signUp(RegisterRequest request) {

        User user = new User();
        user.setUsername(request.getUsername());
        user.setCity(request.getCity());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);
        userRepository.save(user);

        String token = generateVerificationToken(user);
//        mailService.sendEmail(new NotificationEmail("Please activate your account",
//                user.getEmail(),"Thank you for signing up to Spring Reddit " +
//                "plese click on the below url to activate you account :" +
//                "http://localhost:8080/api/auth/accountVerification/"+token));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerifcationToken verifcationToken = new VerifcationToken();
        verifcationToken.setToken(token);
        verifcationToken.setUser(user);

        verificationTokenRepository.save(verifcationToken);

        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerifcationToken> verifcationToken = verificationTokenRepository.findByToken(token);
        verifcationToken.orElseThrow(() -> new SpringCustomException("Invalid Token"));
        fetchUserAndEnable(verifcationToken.get());
    }

    @Transactional
    private void fetchUserAndEnable(VerifcationToken verifcationToken) {
        String username = verifcationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringCustomException("User Not Fount"));
        user.setEnabled(true);
        userRepository.save(user);

    }

    public AuthenticationResponse login(LoginRequest loginRequest) {

        log.info("start logging");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername()
                , loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        log.info("jwt token :" +token);
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginRequest.getUsername())
                .build();
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        log.info("getting current user");
        log.info("getting current user : " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        log.info(principal.getUsername());
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshToken) {
        refreshTokenService.validateRefreshToken(refreshToken.getRefreshToken());
        String token = jwtProvider.generateTokenWithUsername(refreshToken.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshToken.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshToken.getUsername())
                .build();
    }
}
