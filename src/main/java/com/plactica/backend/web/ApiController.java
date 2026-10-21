package com.plactica.backend.web;

import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

	@GetMapping("/me")
	public Map<String, Object> me(@AuthenticationPrincipal Jwt jwt) {
		return Map.of(
			"subject", jwt.getSubject(),
			"scope", jwt.getClaimAsString("scope") != null ? jwt.getClaimAsString("scope") : "",
			"issuer", jwt.getClaimAsString("iss") != null ? jwt.getClaimAsString("iss") : "");
	}

	@GetMapping("/admin/ping")
	public Map<String, String> adminPing() {
		return Map.of("message", "admin access granted");
	}
}
