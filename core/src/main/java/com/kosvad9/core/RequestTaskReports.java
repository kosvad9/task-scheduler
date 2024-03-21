package com.kosvad9.core;

import java.time.LocalDateTime;

public record RequestTaskReports(LocalDateTime from,
                                 LocalDateTime to) {
}
