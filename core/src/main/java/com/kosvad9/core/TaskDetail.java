package com.kosvad9.core;

import java.time.LocalDateTime;

public record TaskDetail(String header,
                         boolean completeStatus,
                         LocalDateTime completeTime) {
}
