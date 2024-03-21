package com.kosvad9.core;

import lombok.Builder;

@Builder
public record EmailMessageEvent(String email,
                                String header,
                                String body) {
}
