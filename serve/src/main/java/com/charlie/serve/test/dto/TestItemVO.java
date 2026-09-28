package com.charlie.serve.test.dto;

import java.time.Instant;

public record TestItemVO(Long id, String name, String description, boolean done, Instant createdAt) {
}
