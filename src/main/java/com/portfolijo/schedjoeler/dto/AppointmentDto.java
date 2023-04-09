package com.portfolijo.schedjoeler.dto;

import com.portfolijo.schedjoeler.domain.Appointment;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a scheduled appointment between a provider and client
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@Builder
public class AppointmentDto extends AppointmentDtoBase {
    @NotNull
    @NonNull
    private UUID uuid;

    @NotNull
    @NonNull
    private LocalDateTime dateTime;

    @NotNull
    @NonNull
    private UserDto provider;

    @NotNull
    @NonNull
    private UserDto client;

    @NotNull
    @NonNull
    private Appointment.Status status;

    private String location;

    private String description;
}
