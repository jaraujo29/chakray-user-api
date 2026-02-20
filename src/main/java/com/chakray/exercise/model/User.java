package com.chakray.exercise.model;

import com.chakray.exercise.validation.AndresFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class User {
    private UUID id;
    private String name;
    private String email;
    @AndresFormat
    private String phone;
    @Pattern(regexp = "^([A-Z&Ñ]{3,4}\\d{6}[A-V1-9][A-Z1-9]\\d)?$",
            message = "tax_id con formato no válido")
    private String tax_id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String created_at;

    private List<Address> addresses;

}
