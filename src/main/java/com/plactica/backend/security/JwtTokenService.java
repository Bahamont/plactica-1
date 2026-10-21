package com.plactica.backend.security;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {

	private final JwtEncoder jwtEncoder;
	private final String issuer;
	private final long expirationMs;

	public JwtTokenService(
			JwtEncoder jwtEncoder,
			@Value("${app.jwt.issuer}") String issuer,
			@Value("${app.jwt.expiration-ms}") long expirationMs) {
		this.jwtEncoder = jwtEncoder;
		this.issuer = issuer;
		this.expirationMs = expirationMs;
	}

	public String generateToken(Authentication authentication) {
		Instant now = Instant.now();
		String scope = authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.filter(authority -> authority.startsWith("ROLE_"))
			.collect(Collectors.joining(" "));

		JwtClaimsSet claims = JwtClaimsSet.builder()
			.issuer(issuer)
			.issuedAt(now)
			.expiresAt(now.plusMillis(expirationMs))
			.subject(authentication.getName())
			.claim("scope", scope)
			.build();

		JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
		return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
	}
}
