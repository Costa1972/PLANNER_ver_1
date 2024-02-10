package ru.costa.entity;

import java.util.UUID;

public record Task(UUID uuid, String details, boolean completed) {

    public Task(String details) {
        this(UUID.randomUUID(), details, false);
    }
}
