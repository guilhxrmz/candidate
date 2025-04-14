package br.com.palo.ti.candidate.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

@Schema(description = "Data Transfer Object for Candidate")
public record candidateDto(
        @Schema(description = "Unique identifier of the candidate", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @NotBlank
        @Schema(description = "Name of the candidate", example = "John Doe", required = true)
        String name,

        @NotBlank
        @Schema(description = "Email address of the candidate", example = "johndoe@example.com", required = true)
        @Email String email,

        @NotBlank
        @Schema(description = "Phone number of the candidate", example = "+1234567890", required = true)
        String phone
) {}