package com.portfolijo.schedjoeler.dto;

import com.portfolijo.schedjoeler.domain.Appointment;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a scheduled appointment between a provider and client
 */
@Getter
@Builder
public class AppointmentDto {
    @NotNull
    private UUID uuid;
    @NotNull
    private LocalDateTime dateTime;
    private String location;
    @NotNull
    private UUID provider;
    @NotNull
    private UUID client;
    @NotNull
    private Appointment.Status status;
    private String description;
}
