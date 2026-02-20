package com.chakray.exercise.model;

import com.chakray.exercise.validation.AndresFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representación del usuario en el sistema")
public class User {

    @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;
    @Schema(example = "Pedro Araujo")
    private String name;
    @Schema(example = "pedro.araujo@example.com")
    private String email;
    @AndresFormat
    @Schema(example = "+52 55 1234 5678")
    private String phone;
    @Pattern(regexp = "^([A-Z&Ñ]{3,4}\\d{6}[A-V1-9][A-Z1-9]\\d)?$",
            message = "tax_id con formato no válido")
    @Schema(example = "SARM8101017H1")
    private String tax_id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(example = "P@ssword2024", description = "La contraseña se oculta en las respuestas")
    private String password;

    @Schema(example = "2024-05-20T10:00:00Z")
    private String created_at;

    private List<Address> addresses;

}
