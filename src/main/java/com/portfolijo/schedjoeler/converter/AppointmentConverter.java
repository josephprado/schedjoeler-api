package com.portfolijo.schedjoeler.converter;

import com.portfolijo.schedjoeler.domain.Appointment;
import com.portfolijo.schedjoeler.dto.AppointmentCreateDto;
import com.portfolijo.schedjoeler.dto.AppointmentDto;
import com.portfolijo.schedjoeler.dto.AppointmentUpdateDto;
import com.portfolijo.schedjoeler.service.AppointmentService;
import com.portfolijo.schedjoeler.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Converts {@link Appointment} objects to/from DTO representations
 */
@RequiredArgsConstructor
@Component
public class AppointmentConverter {
    private final AppointmentService svc;
    private final UserService uSvc;
    private final UserConverter uCon;

    /**
     * Creates an appointment DTO based on the given appointment
     *
     * @param appointment An appointment
     * @return An appointment DTO
     */
    public AppointmentDto toDto(Appointment appointment) {
        return AppointmentDto
                .builder()
                .uuid(appointment.getUuid())
                .dateTime(appointment.getDateTime())
                .provider(uCon.toDto(appointment.getProvider()))
                .client(uCon.toDto(appointment.getClient()))
                .status(appointment.getStatus())
                .location(appointment.getLocation())
                .description(appointment.getDescription())
                .build();
    }

    /**
     * Creates an appointment based on the given appointment create DTO
     *
     * @param dto An appointment create DTO
     * @return An appointment
     */
    public Appointment toAppointment(AppointmentCreateDto dto) {
        return Appointment
                .builder()
                .uuid(UUID.randomUUID())
                .dateTime(dto.getDateTime())
                .provider(uSvc.findOne(dto.getProvider()))
                .client(uSvc.findOne(dto.getClient()))
                .status(Appointment.Status.NEW)
                .location(dto.getLocation())
                .description(dto.getDescription())
                .build();
    }

    /**
     * Creates an updated version of the identified appointment based on the given appointment update DTO
     *
     * @param uuid An appointment uuid
     * @param dto An appointment update DTO
     * @return An appointment
     */
    public Appointment toAppointment(UUID uuid, AppointmentUpdateDto dto) {
        Appointment updated = svc.findOne(uuid);

        LocalDateTime dateTime = dto.getDateTime();
        if (dateTime != null)
            updated.setDateTime(dateTime);

        UUID pUuid = dto.getProvider();
        if (pUuid != null)
            updated.setProvider(uSvc.findOne(pUuid));

        UUID cUuid = dto.getClient();
        if (cUuid != null)
            updated.setClient(uSvc.findOne(cUuid));

        Appointment.Status status = dto.getStatus();
        if (status != null)
            updated.setStatus(status);

        String location = dto.getLocation();
        if (location != null)
            updated.setLocation(location);

        String description = dto.getDescription();
        if (description != null)
            updated.setDescription(description);

        return updated;
    }
}
