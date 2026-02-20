package com.chakray.exercise.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(
        @Schema(description = "RFC del usuario (tax_id)", example = "GARR8203157T1")
        String username,

        @Schema(description = "Contraseña del usuario", example = "PasswordSafe123")
        String password
) {}