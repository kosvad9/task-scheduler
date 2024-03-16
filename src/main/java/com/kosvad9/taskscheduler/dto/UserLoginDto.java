package com.kosvad9.taskscheduler.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record UserLoginDto(
        @Email
        String email,
        @NotEmpty
        String password) {
}
