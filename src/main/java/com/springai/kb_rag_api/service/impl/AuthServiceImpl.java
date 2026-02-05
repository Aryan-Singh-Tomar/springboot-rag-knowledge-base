package com.springai.kb_rag_api.service.impl;

import com.springai.kb_rag_api.dto.LoginRequest;
import com.springai.kb_rag_api.dto.RefreshRequest;
import com.springai.kb_rag_api.dto.TokenResponse;
import com.springai.kb_rag_api.entity.RefreshTokenEntity;
import com.springai.kb_rag_api.exception.BadRequestException;
import com.springai.kb_rag_api.repository.RefreshTokenRepository;
import com.springai.kb_rag_api.repository.UserRepository;
import com.springai.kb_rag_api.security.TokenHashing;
import com.springai.kb_rag_api.service.AuthService;
import com.springai.kb_rag_api.service.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    @Override
    public TokenResponse login(LoginRequest request) {
        var user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        if (!encoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Invalid credentials");
        }
        String access = jwtService.generateAccessToken(user.getId(), user.getUsername(), user.getRole());
        String refresh = jwtService.generateRefreshToken(user.getId(), user.getUsername());
        String jti = jwtService.extractJti(refresh);
        Instant exp = jwtService.extractExpiry(refresh);

        RefreshTokenEntity rt = new RefreshTokenEntity();
        rt.setUserId(user.getId());
        rt.setJti(jti);
        rt.setTokenHash(TokenHashing.sha256(refresh));
        rt.setExpiresAt(exp);
        refreshTokenRepository.save(rt);
        return new TokenResponse(access, refresh);
    }

    @Override
    public TokenResponse refresh(RefreshRequest request) {
        String refreshToken = request.getRefreshToken();
        Long uid = jwtService.extractUserIdFromRefreshTokenOrThrow(refreshToken);
        String jti = jwtService.extractJti(refreshToken);

        RefreshTokenEntity stored = refreshTokenRepository.findByJti(jti)
                .orElseThrow(() -> new BadRequestException("Invalid refresh token"));

        if (stored.isRevoked()) throw new BadRequestException("Refresh token revoked");
        if (stored.getExpiresAt().isBefore(Instant.now())) throw new BadRequestException("Refresh token expired");

        String hash = TokenHashing.sha256(refreshToken);
        if (!hash.equals(stored.getTokenHash())) throw new BadRequestException("Invalid refresh token");

        // revoke old
        stored.setRevoked(true);
        refreshTokenRepository.save(stored);

        var user = userRepo.findById(uid)
                .orElseThrow(() -> new BadRequestException("Invalid refresh token"));

        String newAccess = jwtService.generateAccessToken(user.getId(), user.getUsername(), user.getRole());
        String newRefresh = jwtService.generateRefreshToken(user.getId(), user.getUsername());

        // store new refresh
        RefreshTokenEntity rt = new RefreshTokenEntity();
        rt.setUserId(user.getId());
        rt.setJti(jwtService.extractJti(newRefresh));
        rt.setTokenHash(TokenHashing.sha256(newRefresh));
        rt.setExpiresAt(jwtService.extractExpiry(newRefresh));
        refreshTokenRepository.save(rt);

        return new TokenResponse(newAccess, newRefresh);
    }

    @Override
    public void logout(RefreshRequest request) {
        String token = request.getRefreshToken();
        String jti = jwtService.extractJti(token);

         refreshTokenRepository.findByJti(jti).ifPresent(rt -> {
            rt.setRevoked(true);
            refreshTokenRepository.save(rt);
        });
    }
}
