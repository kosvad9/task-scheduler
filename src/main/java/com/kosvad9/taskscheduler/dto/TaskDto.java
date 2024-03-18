package com.kosvad9.taskscheduler.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TaskDto(
                      @NotNull
                      Long id,
                      String header,
                      String text,
                      boolean completeStatus,
                      LocalDateTime completeTime) {
    public TaskDto {
    }

    public TaskDto(String header, String text, boolean completeStatus, LocalDateTime completeTime) {
        this(null, header, text, completeStatus, completeTime);
    }
}
