package com.kosvad9.core;

import java.util.List;

public record TaskReport(String email,
                         List<TaskDetail> tasks) {
}
