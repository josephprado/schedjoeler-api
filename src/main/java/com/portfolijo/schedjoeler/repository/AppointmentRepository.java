package com.portfolijo.schedjoeler.repository;

import com.portfolijo.schedjoeler.domain.Appointment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Provides CRUD operations for {@link Appointment} entities
 */
@Repository
public interface AppointmentRepository extends
        JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {

    /**
     * Checks if the given uuid identifies an existing appointment
     *
     * @param uuid An appointment uuid
     * @return True if the appointment exists, or false otherwise
     */
    boolean existsByUuid(UUID uuid);

    /**
     * Finds the identified appointment
     *
     * @param uuid An appointment uuid
     * @return An appointment
     */
    Appointment findByUuid(UUID uuid);

    /**
     * Finds all appointments matching the given specification
     *
     * @param spec A specification for an appointment, can be null
     * @param sort A sorting method, must not be null
     * @return A list of appointments
     */
    List<Appointment> findAll(Specification<Appointment> spec, Sort sort);

    /**
     * Deletes the identified appointment
     *
     * @param uuid An appointment uuid
     * @return The number of deleted records
     */
    int deleteByUuid(UUID uuid);
}
