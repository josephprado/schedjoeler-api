package com.portfolijo.schedjoeler.service;

import com.portfolijo.schedjoeler.domain.Appointment;
import com.portfolijo.schedjoeler.domain.User;
import com.portfolijo.schedjoeler.repository.AppointmentRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {
    AppointmentRepository repo;
    UserService uSvc;
    AppointmentService svc;
    User provider;
    User client;

    @BeforeEach
    void setup() {
        repo = mock(AppointmentRepository.class);
        uSvc = mock(UserService.class);
        svc = new AppointmentService(repo, uSvc);
        provider = mock(User.class);
        client = mock(User.class);
    }

    @Nested
    class AssertAppointmentExists {
        @Test
        void does_not_throw_exception_if_appointment_exists() {
            UUID uuid = UUID.randomUUID();
            when(repo.existsByUuid(uuid)).thenReturn(true);
            assertDoesNotThrow(() -> svc.assertAppointmentExists(uuid));
        }

        @Test
        void throws_NoSuchElementException_if_appointment_does_not_exist() {
            when(repo.existsByUuid(any(UUID.class))).thenReturn(false);
            assertThrows(NoSuchElementException.class, () -> svc.assertAppointmentExists((UUID.randomUUID())));
        }
    }

    @Nested
    class FindOne {
        @Test
        void can_find_by_uuid() {
            UUID uuid = UUID.randomUUID();
            Appointment expected = Appointment
                    .builder()
                    .id(1L)
                    .uuid(uuid)
                    .dateTime(LocalDateTime.now())
                    .provider(provider)
                    .client(client)
                    .status(Appointment.Status.NEW)
                    .location("a")
                    .description("b")
                    .build();
            when(repo.findByUuid(uuid)).thenReturn(expected);
            Appointment actual = svc.findOne(uuid);
            assertEquals(expected, actual);
        }

        @Test
        void throws_NoSuchElementException_if_not_found() {
            when(repo.findByUuid(any(UUID.class))).thenReturn(null);
            assertThrows(NoSuchElementException.class, () -> svc.findOne(UUID.randomUUID()));
        }
    }

    @Nested
    class FindAll {
        @Test
        void can_find_all() {
            List<Appointment> expected = new ArrayList<>();

            for (int i = 0; i < 3; i++) {
                expected.add(Appointment
                        .builder()
                        .id((long) i)
                        .uuid(UUID.randomUUID())
                        .dateTime(LocalDateTime.now())
                        .provider(provider)
                        .client(client)
                        .status(Appointment.Status.NEW)
                        .location("a")
                        .description("b")
                        .build());
            }

            when(repo.findAll(any(Specification.class), any(Sort.class))).thenReturn(expected);
            List<Appointment> actual = svc.findAll(null, null, null, null);
            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }

        // TODO: create tests using specifications

        @Test
        void returns_empty_list_when_none_found() {
            List<Appointment> expected = List.of();
            when(repo.findAll(any(Specification.class), any(Sort.class))).thenReturn(expected);
            List<Appointment> actual = svc.findAll(null, null, null, null);
            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }
    }

    @Nested
    class SaveOne {
        @Test
        void returns_saved_appointment() {
            Appointment expected = Appointment
                    .builder()
                    .id(1L)
                    .uuid(UUID.randomUUID())
                    .dateTime(LocalDateTime.now())
                    .provider(provider)
                    .client(client)
                    .status(Appointment.Status.NEW)
                    .location("a")
                    .description("b")
                    .build();
            when(repo.save(expected)).thenReturn(expected);
            Appointment actual = svc.saveOne(expected);
            assertEquals(expected, actual);
        }
    }

    @Nested
    class DeleteOne {
        @Test
        void does_not_throw_exception_if_deletion_successful() {
            UUID uuid = UUID.randomUUID();
            when(repo.existsByUuid(uuid)).thenReturn(true);
            when(repo.deleteByUuid(uuid)).thenReturn(1);
            assertDoesNotThrow(() -> svc.deleteOne(uuid));
        }

        @Test
        void throws_NoSuchElementException_if_appointment_does_not_exist() {
            when(repo.existsByUuid(any(UUID.class))).thenReturn(false);
            assertThrows(NoSuchElementException.class, () -> svc.deleteOne(UUID.randomUUID()));
        }

        @Test
        void throws_Exception_if_deletion_unsuccessful() {
            UUID uuid = UUID.randomUUID();
            when(repo.existsByUuid(uuid)).thenReturn(true);
            when(repo.deleteByUuid(uuid)).thenReturn(0);
            assertThrows(Exception.class, () -> svc.deleteOne(uuid));
        }
    }
}