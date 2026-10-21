package com.plactica.backend.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plactica.backend.security.JwtTokenService;
import com.plactica.backend.web.dto.LoginRequest;
import com.plactica.backend.web.dto.TokenResponse;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final JwtTokenService jwtTokenService;
	private final long expirationMs;

	public AuthController(
			AuthenticationManager authenticationManager,
			JwtTokenService jwtTokenService,
			@Value("${app.jwt.expiration-ms}") long expirationMs) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenService = jwtTokenService;
		this.expirationMs = expirationMs;
	}

	@PostMapping("/login")
	public ResponseEntity<TokenResponse> login(@RequestBody @Validated LoginRequest request) {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(request.username(), request.password()));

		String token = jwtTokenService.generateToken(authentication);
		return ResponseEntity.ok(new TokenResponse(token, "Bearer", expirationMs));
	}
}
