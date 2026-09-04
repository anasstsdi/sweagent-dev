package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Request body for creating / updating a user.
 * Keeps the JPA entity off the API surface: the client never sends an {@code id},
 * and validation lives here rather than on the persisted entity.
 */
public record UserInput(
        @NotBlank String name,
        @NotBlank @Email String email
) {
}
