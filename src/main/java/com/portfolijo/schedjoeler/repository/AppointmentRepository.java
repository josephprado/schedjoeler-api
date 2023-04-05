package com.portfolijo.schedjoeler.repository;

import com.portfolijo.schedjoeler.domain.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Provides CRUD operations for {@link Appointment} entities
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
