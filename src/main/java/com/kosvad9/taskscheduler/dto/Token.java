package com.kosvad9.taskscheduler.dto;

import java.time.Instant;
import java.util.UUID;

public record Token(UUID id,
                    Long subjectId,
                    Instant createdAt,
                    Instant expiresAt) {
}
