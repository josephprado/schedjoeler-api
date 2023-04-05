package com.portfolijo.schedjoeler.service;

import com.portfolijo.schedjoeler.domain.Appointment;
import com.portfolijo.schedjoeler.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Provides services for handling {@link Appointment} entities
 */
@RequiredArgsConstructor
@Service
public class AppointmentService {
    private final AppointmentRepository REPO;
}
