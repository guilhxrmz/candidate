package br.com.palo.ti.candidate.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

@Schema(description = "Data Transfer Object for status")
public record statusDto(
        @Schema(description = "Email of the candidate", example = "test132@gmail.com")
        @Email String email,

        @NotBlank
        @Schema(description = "Interview status", example = "CLOSED", required = true)
        String status
) {}