package com.portfolijo.schedjoeler.controller;

import com.portfolijo.schedjoeler.domain.Appointment;
import com.portfolijo.schedjoeler.dto.AppointmentCreateDto;
import com.portfolijo.schedjoeler.dto.AppointmentUpdateDto;
import com.portfolijo.schedjoeler.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Handles requests for {@link Appointment} resources
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController extends Controller {
    private final AppointmentService SVC;

    @GetMapping("/{uuid}")
    public ResponseEntity<Response> getOne(@PathVariable(name = "uuid") UUID uuid) {
        return null;
    }

    @GetMapping("")
    public ResponseEntity<Response> getAll() {
        return null;
    }

    @PostMapping("")
    public ResponseEntity<Response> saveOne(@RequestBody AppointmentCreateDto dto) {
        return null;
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<Response> updateOne(@PathVariable(name = "uuid") UUID uuid,
                                              @RequestBody AppointmentUpdateDto dto) {
        return null;
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Response> deleteOne(@PathVariable(name = "uuid") UUID uuid) {
        return null;
    }
}
