package com.plactica.backend.web.dto;

public record TokenResponse(
		String accessToken,
		String tokenType,
		long expiresInMs) {
}
