package com.portfolijo.schedjoeler.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a new appointment between a provider and client
 */
@Getter
@Builder
public class AppointmentCreateDto extends AppointmentDtoBase {
    @NotNull
    private LocalDateTime dateTime;
    private String location;
    @NotNull
    private UUID provider;
    @NotNull
    private UUID client;
    private String description;
}
