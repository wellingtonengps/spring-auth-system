package com.akin.akinbackend.dto;

import lombok.Builder;

@Builder
public record JwtResponseDTO(String accessToken, String refreshToken) {
}
