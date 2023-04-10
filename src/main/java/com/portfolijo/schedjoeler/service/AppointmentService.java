package com.portfolijo.schedjoeler.service;

import com.portfolijo.schedjoeler.domain.Appointment;
import com.portfolijo.schedjoeler.domain.User;
import com.portfolijo.schedjoeler.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Provides services for handling {@link Appointment} entities
 */
@RequiredArgsConstructor
@Service
public class AppointmentService {
    private final AppointmentRepository REPO;
    private final UserService U_SVC;

    /**
     * Verifies that the identified appointment exists
     *
     * @param uuid An appointment uuid
     * @throws NoSuchElementException If the appointment does not exist
     */
    public void assertAppointmentExists(UUID uuid) throws NoSuchElementException {
        if (!REPO.existsByUuid(uuid))
            handleAppointmentNotFound(uuid);
    }

    /**
     * Finds the identified appointment
     *
     * @param uuid An appointment uuid
     * @return An appointment
     * @throws NoSuchElementException If the appointment does not exist
     */
    public Appointment findOne(UUID uuid) throws NoSuchElementException {
        Appointment appointment = REPO.findByUuid(uuid);

        if (appointment == null)
            handleAppointmentNotFound(uuid);

        return appointment;
    }

    /**
     * Finds all appointments matching the given criteria. Passing null to any parameter ignores that field
     * from the search query
     *
     * @param user A user uuid, can be null
     * @param from A minimum bounding date/time (inclusive), can be null
     * @param to A maximum bounding date/time (inclusive), can be null
     * @param status An appointment status, can be null
     * @return A list of appointments
     * @throws NoSuchElementException If a user with the given uuid does not exist
     */
    public List<Appointment> findAll(UUID user, LocalDateTime from, LocalDateTime to, Appointment.Status status)
            throws NoSuchElementException {

        if (user != null)
            U_SVC.assertUserExists(user);

        return REPO.findAll(
                Specification
                        .where(involvesUser(user))
                        .and(occursOnOrAfter(from))
                        .and(occursOnOrBefore(to))
                        .and(statusEquals(status)),
                Sort.by(Appointment.Fields.dateTime).ascending());
    }

    /**
     * Saves the given appointment
     *
     * @param appointment An appointment
     * @return The saved appointment
     */
    @Transactional
    @Modifying
    public Appointment saveOne(Appointment appointment) {
        return REPO.save(appointment);
    }

    /**
     * Deletes the identified appointment
     *
     * @param uuid An appointment uuid
     * @throws NoSuchElementException If the appointment does not exist
     * @throws Exception If the deletion was unsuccessful
     */
    @Transactional
    @Modifying
    public void deleteOne(UUID uuid) throws Exception {
        assertAppointmentExists(uuid);

        if (REPO.deleteByUuid(uuid) == 0)
            throw new Exception("Unable to delete appointment uuid="+uuid+".");
    }

    /**
     * Throws an exception indicating that no appointment was found with the given uuid
     *
     * @param uuid An appointment uuid
     * @throws NoSuchElementException Unconditionally
     */
    private void handleAppointmentNotFound(UUID uuid) throws NoSuchElementException {
        throw new NoSuchElementException("Appointment uuid="+uuid+" not found.");
    }

    /**
     * Creates a specification for an appointment involving the identified user
     *
     * @param uuid A user uuid
     * @return A specification
     */
    private Specification<Appointment> involvesUser(UUID uuid) {
        return ((root, query, criteriaBuilder) -> {
            if (uuid == null)
                return criteriaBuilder.conjunction();

            return criteriaBuilder.or(
                    criteriaBuilder.equal(root.get(Appointment.Fields.provider).get(User.Fields.uuid), uuid),
                    criteriaBuilder.equal(root.get(Appointment.Fields.client).get(User.Fields.uuid), uuid));
        });
    }

    /**
     * Creates a specification for an appointment occurring on or after the given date/time
     *
     * @param dateTime A minimum bounding date/time (inclusive)
     * @return A specification
     */
    private Specification<Appointment> occursOnOrAfter(LocalDateTime dateTime) {
        return ((root, query, criteriaBuilder) ->
                dateTime == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.greaterThanOrEqualTo(root.get(Appointment.Fields.dateTime), dateTime));
    }

    /**
     * Creates a specification for an appointment occurring on or before the given date/time
     *
     * @param dateTime A maximum bounding date/time (inclusive)
     * @return A specification
     */
    private Specification<Appointment> occursOnOrBefore(LocalDateTime dateTime) {
        return ((root, query, criteriaBuilder) ->
                dateTime == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.lessThanOrEqualTo(root.get(Appointment.Fields.dateTime), dateTime));
    }

    /**
     * Creates a specification for an appointment with the given status
     *
     * @param status An appointment status
     * @return A specification
     */
    private Specification<Appointment> statusEquals(Appointment.Status status) {
        return ((root, query, criteriaBuilder) ->
                status == null
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.equal(root.get(Appointment.Fields.status), status));
    }
}
