package com.portfolijo.schedjoeler.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a new appointment between a provider and client
 */
@Getter
@Builder
public class AppointmentCreateDto extends AppointmentDtoBase {
    @NotNull
    @NonNull
    private LocalDateTime dateTime;

    @NotNull
    @NonNull
    private UUID provider;

    @NotNull
    @NonNull
    private UUID client;

    private String location;

    private String description;
}
