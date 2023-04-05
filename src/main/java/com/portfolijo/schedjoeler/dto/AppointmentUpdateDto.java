package com.portfolijo.schedjoeler.dto;

import com.portfolijo.schedjoeler.domain.Appointment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents an update to an existing appointment
 */
@Getter
@Builder
public class AppointmentUpdateDto extends AppointmentDtoBase {
    private LocalDateTime dateTime;
    private String location;
    private UUID provider;
    private UUID client;
    private Appointment.Status status;
    private String description;
}
