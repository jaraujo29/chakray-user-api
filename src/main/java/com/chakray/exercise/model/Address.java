package com.chakray.exercise.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información de la dirección del usuario")
public class Address {
    @Schema(example = "1")
    private Integer id;
    @Schema(example = "Casa")
    private String name;
    @Schema(example = "Av. Insurgentes Sur 123")
    private String street;
    @Schema(example = "MX")
    private String country_code;
}
