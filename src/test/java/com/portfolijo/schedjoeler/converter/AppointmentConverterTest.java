package com.portfolijo.schedjoeler.converter;

import com.portfolijo.schedjoeler.domain.Appointment;
import com.portfolijo.schedjoeler.domain.User;
import com.portfolijo.schedjoeler.dto.AppointmentCreateDto;
import com.portfolijo.schedjoeler.dto.AppointmentDto;
import com.portfolijo.schedjoeler.dto.AppointmentUpdateDto;
import com.portfolijo.schedjoeler.dto.UserDto;
import com.portfolijo.schedjoeler.repository.AppointmentRepository;
import com.portfolijo.schedjoeler.service.AppointmentService;
import com.portfolijo.schedjoeler.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class AppointmentConverterTest {
    AppointmentRepository repo;
    UserService uSvc;
    AppointmentService svc;
    UserConverter uCon;
    AppointmentConverter con;

    @BeforeEach
    void setup() {
        repo = mock(AppointmentRepository.class);
        uSvc = mock(UserService.class);
        svc = new AppointmentService(repo, uSvc);
        uCon = mock(UserConverter.class);
        con = new AppointmentConverter(svc, uSvc, uCon);
    }

    @Nested
    class ToDto {
        @Test
        void can_convert_appointment_to_dto() {
            User provider = mock(User.class);
            User client = mock(User.class);
            UserDto providerDto = mock(UserDto.class);
            UserDto clientDto = mock(UserDto.class);
            UUID uuid = UUID.randomUUID();
            LocalDateTime now = LocalDateTime.now();

            Appointment appointment = Appointment
                    .builder()
                    .id(1L)
                    .uuid(uuid)
                    .dateTime(now)
                    .provider(provider)
                    .client(client)
                    .status(Appointment.Status.NEW)
                    .location("a")
                    .description("b")
                    .build();

            AppointmentDto expected = AppointmentDto
                    .builder()
                    .uuid(uuid)
                    .dateTime(now)
                    .provider(providerDto)
                    .client(clientDto)
                    .status(Appointment.Status.NEW)
                    .location("a")
                    .description("b")
                    .build();

            when(uCon.toDto(provider)).thenReturn(providerDto);
            when(uCon.toDto(client)).thenReturn(clientDto);

            AppointmentDto actual = con.toDto(appointment);
            assertEquals(expected, actual);
        }
    }

    @Nested
    class ToAppointmentFromCreate {
        @Test
        void can_convert_appointment_create_dto_to_appointment() {
            User provider = mock(User.class);
            User client = mock(User.class);
            UUID pUuid = UUID.randomUUID();
            UUID cUuid = UUID.randomUUID();
            LocalDateTime now = LocalDateTime.now();

            AppointmentCreateDto dto = AppointmentCreateDto
                    .builder()
                    .dateTime(now)
                    .provider(pUuid)
                    .client(cUuid)
                    .location("a")
                    .description("b")
                    .build();

            Appointment expected = Appointment
                    .builder()
                    .uuid(UUID.randomUUID())
                    .dateTime(now)
                    .provider(provider)
                    .client(client)
                    .status(Appointment.Status.NEW)
                    .location("a")
                    .description("b")
                    .build();

            when(uSvc.findOne(pUuid)).thenReturn(provider);
            when(uSvc.findOne(cUuid)).thenReturn(client);

            Appointment actual = con.toAppointment(dto);

            // toAppointment(AppointmentCreateDto) generates a random uuid at runtime, so cannot simply test
            // assertEquals(expected, actual) since the uuids will differ
            assertNotNull(actual.getUuid());
            assertEquals(expected.getDateTime(), actual.getDateTime());
            assertEquals(expected.getProvider(), actual.getProvider());
            assertEquals(expected.getClient(), actual.getClient());
            assertEquals(expected.getStatus(), actual.getStatus());
            assertEquals(expected.getLocation(), actual.getLocation());
            assertEquals(expected.getDescription(), actual.getDescription());
        }
    }

    @Nested
    class ToAppointmentFromUpdate {
        @Test
        void returns_existing_appointment() {
            User provider = mock(User.class);
            User client = mock(User.class);
            UUID uuid = UUID.randomUUID();
            AppointmentUpdateDto dto = AppointmentUpdateDto.builder().build();

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

            Appointment actual = con.toAppointment(uuid, dto);
            assertEquals(expected, actual);
        }

        static User provider1 = mock(User.class);
        static User client1 = mock(User.class);
        static UUID pUuid1 = UUID.randomUUID();
        static UUID cUuid1 = UUID.randomUUID();
        static UUID uuid1 = UUID.randomUUID();
        static LocalDateTime now = LocalDateTime.now();

        @ParameterizedTest
        @MethodSource("g_converts_existing_appointment")
        void converts_existing_appointment(AppointmentUpdateDto dto, Appointment existing, Appointment expected) {
            when(repo.findByUuid(uuid1)).thenReturn(existing);
            lenient().when(uSvc.findOne(pUuid1)).thenReturn(provider1);
            lenient().when(uSvc.findOne(cUuid1)).thenReturn(client1);

            Appointment actual = con.toAppointment(uuid1, dto);
            assertEquals(expected, actual);
        }

        static Stream<Arguments> g_converts_existing_appointment() {
            return Stream.of(
                    Arguments.of(
                            AppointmentUpdateDto.builder().build(),
                            Appointment.builder()
                                    .uuid(uuid1)
                                    .dateTime(now)
                                    .provider(provider1)
                                    .client(client1)
                                    .status(Appointment.Status.NEW)
                                    .location("a")
                                    .description("b")
                                    .build(),
                            Appointment.builder()
                                    .uuid(uuid1)
                                    .dateTime(now)
                                    .provider(provider1)
                                    .client(client1)
                                    .status(Appointment.Status.NEW)
                                    .location("a")
                                    .description("b")
                                    .build()),
                    Arguments.of(
                            AppointmentUpdateDto.builder().dateTime(LocalDate.of(2023, 1, 1).atStartOfDay()).build(),
                            Appointment.builder()
                                    .uuid(uuid1)
                                    .dateTime(now)
                                    .provider(provider1)
                                    .client(client1)
                                    .status(Appointment.Status.NEW)
                                    .location("a")
                                    .description("b")
                                    .build(),
                            Appointment.builder()
                                    .uuid(uuid1)
                                    .dateTime(LocalDate.of(2023, 1, 1).atStartOfDay())
                                    .provider(provider1)
                                    .client(client1)
                                    .status(Appointment.Status.NEW)
                                    .location("a")
                                    .description("b")
                                    .build()),
                    Arguments.of(
                            AppointmentUpdateDto.builder().provider(cUuid1).build(),
                            Appointment.builder()
                                    .uuid(uuid1)
                                    .dateTime(now)
                                    .provider(provider1)
                                    .client(client1)
                                    .status(Appointment.Status.NEW)
                                    .location("a")
                                    .description("b")
                                    .build(),
                            Appointment.builder()
                                    .uuid(uuid1)
                                    .dateTime(now)
                                    .provider(client1)
                                    .client(client1)
                                    .status(Appointment.Status.NEW)
                                    .location("a")
                                    .description("b")
                                    .build()),
                    Arguments.of(
                            AppointmentUpdateDto.builder().client(pUuid1).build(),
                            Appointment.builder()
                                    .uuid(uuid1)
                                    .dateTime(now)
                                    .provider(provider1)
                                    .client(client1)
                                    .status(Appointment.Status.NEW)
                                    .location("a")
                                    .description("b")
                                    .build(),
                            Appointment.builder()
                                    .uuid(uuid1)
                                    .dateTime(now)
                                    .provider(provider1)
                                    .client(provider1)
                                    .status(Appointment.Status.NEW)
                                    .location("a")
                                    .description("b")
                                    .build()),
                    Arguments.of(
                            AppointmentUpdateDto.builder().status(Appointment.Status.COMPLETE).build(),
                            Appointment.builder()
                                    .uuid(uuid1)
                                    .dateTime(now)
                                    .provider(provider1)
                                    .client(client1)
                                    .status(Appointment.Status.NEW)
                                    .location("a")
                                    .description("b")
                                    .build(),
                            Appointment.builder()
                                    .uuid(uuid1)
                                    .dateTime(now)
                                    .provider(provider1)
                                    .client(client1)
                                    .status(Appointment.Status.COMPLETE)
                                    .location("a")
                                    .description("b")
                                    .build()),
                    Arguments.of(
                            AppointmentUpdateDto.builder().location("1").build(),
                            Appointment.builder()
                                    .uuid(uuid1)
                                    .dateTime(now)
                                    .provider(provider1)
                                    .client(client1)
                                    .status(Appointment.Status.NEW)
                                    .location("a")
                                    .description("b")
                                    .build(),
                            Appointment.builder()
                                    .uuid(uuid1)
                                    .dateTime(now)
                                    .provider(provider1)
                                    .client(client1)
                                    .status(Appointment.Status.NEW)
                                    .location("1")
                                    .description("b")
                                    .build()),
                    Arguments.of(
                            AppointmentUpdateDto.builder().description("1").build(),
                            Appointment.builder()
                                    .uuid(uuid1)
                                    .dateTime(now)
                                    .provider(provider1)
                                    .client(client1)
                                    .status(Appointment.Status.NEW)
                                    .location("a")
                                    .description("b")
                                    .build(),
                            Appointment.builder()
                                    .uuid(uuid1)
                                    .dateTime(now)
                                    .provider(provider1)
                                    .client(client1)
                                    .status(Appointment.Status.NEW)
                                    .location("a")
                                    .description("1")
                                    .build())
            );
        }
    }
}