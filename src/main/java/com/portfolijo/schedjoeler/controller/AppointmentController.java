package com.portfolijo.schedjoeler.controller;

import com.portfolijo.schedjoeler.converter.AppointmentConverter;
import com.portfolijo.schedjoeler.domain.Appointment;
import com.portfolijo.schedjoeler.dto.AppointmentCreateDto;
import com.portfolijo.schedjoeler.dto.AppointmentDto;
import com.portfolijo.schedjoeler.dto.AppointmentDtoBase;
import com.portfolijo.schedjoeler.dto.AppointmentUpdateDto;
import com.portfolijo.schedjoeler.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Handles requests for {@link Appointment} resources
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController extends Controller<AppointmentDtoBase> {
    private final AppointmentService SVC;
    private final AppointmentConverter CON;

    /**
     * Gets the identified appointment
     *
     * @param uuid An appointment uuid
     * @return A response entity containing an appointment
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<Response<AppointmentDto>> getOne(@PathVariable(name = "uuid") UUID uuid) {
        Appointment appointment = SVC.findOne(uuid);
        AppointmentDto data = CON.toDto(appointment);
        return responseCodeOk(List.of(data));
    }

    /**
     * Gets all appointments matching the given criteria
     *
     * @param user A user uuid, can be null
     * @param from A minimum bounding date/time (inclusive), can be null
     * @param to A maximum bounding date/time (inclusive), can be null
     * @param status An appointment status, can be null
     * @return A response entity containing a list of appointments
     */
    @GetMapping("")
    public ResponseEntity<Response<AppointmentDto>> getAll(
            @RequestParam(name = "user", required = false) UUID user,
            @RequestParam(name = "from", required = false) LocalDateTime from,
            @RequestParam(name = "to", required = false) LocalDateTime to,
            @RequestParam(name = "status", required = false) Appointment.Status status) {

        List<AppointmentDto> data = SVC
                .findAll(user, from, to, status)
                .stream()
                .map(CON::toDto)
                .toList();
        return responseCodeOk(data);
    }

    /**
     * Saves the given appointment
     *
     * @param dto An appointment create DTO
     * @return A response entity containing the saved appointment
     */
    @PostMapping("")
    public ResponseEntity<Response<AppointmentDto>> saveOne(@Valid @RequestBody AppointmentCreateDto dto) {
        Appointment appointment = CON.toAppointment(dto);
        appointment = SVC.saveOne(appointment);
        AppointmentDto data = CON.toDto(appointment);
        return responseCodeCreated(List.of(data), "/"+data.getUuid());
    }

    /**
     * Updates the identified appointment
     *
     * @param uuid An appointment uuid
     * @param dto An appointment update DTO
     * @return A response entity containing the updated appointment
     */
    @PatchMapping("/{uuid}")
    public ResponseEntity<Response<AppointmentDto>> updateOne(@PathVariable(name = "uuid") UUID uuid,
                                                              @Valid @RequestBody AppointmentUpdateDto dto) {

        Appointment appointment = CON.toAppointment(uuid, dto);
        appointment = SVC.saveOne(appointment);
        AppointmentDto data = CON.toDto(appointment);
        return responseCodeOk(List.of(data));
    }

    /**
     * Deletes the identified appointment
     *
     * @param uuid An appointment uuid
     * @return A response entity containing the result of the deletion
     * @throws Exception If the deletion was unsuccessful
     */
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Response<AppointmentDto>> deleteOne(@PathVariable(name = "uuid") UUID uuid) throws Exception {
        SVC.deleteOne(uuid);
        return responseCodeNoContent();
    }
}
