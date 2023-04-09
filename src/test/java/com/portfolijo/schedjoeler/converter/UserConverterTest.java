package com.portfolijo.schedjoeler.converter;

import com.portfolijo.schedjoeler.domain.User;
import com.portfolijo.schedjoeler.dto.UserCreateDto;
import com.portfolijo.schedjoeler.dto.UserDto;
import com.portfolijo.schedjoeler.dto.UserUpdateDto;
import com.portfolijo.schedjoeler.repository.UserRepository;
import com.portfolijo.schedjoeler.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class UserConverterTest {
    UserRepository repo;
    UserService svc;
    UserConverter con;

    @BeforeEach
    void setup() {
        repo = mock(UserRepository.class);
        svc = new UserService(repo);
        con = new UserConverter(svc);
    }

    @Nested
    class ToDto {
        @Test
        void can_convert_user_to_dto() {
            UUID uuid = UUID.randomUUID();
            User user = User
                    .builder()
                    .id(1L)
                    .uuid(uuid)
                    .firstName("a")
                    .lastName("b")
                    .email("c")
                    .phone("d")
                    .build();

            UserDto expected = UserDto
                    .builder()
                    .uuid(uuid)
                    .firstName("a")
                    .lastName("b")
                    .email("c")
                    .phone("d")
                    .build();

            UserDto actual = con.toDto(user);
            assertEquals(actual, expected);
        }
    }

    @Nested
    class ToUserFromCreate {
        @Test
        void can_convert_user_create_dto_to_user() {
            UserCreateDto dto = UserCreateDto
                    .builder()
                    .firstName("a")
                    .lastName("b")
                    .email("c")
                    .phone("d")
                    .build();

            User expected = User
                    .builder()
                    .uuid(UUID.randomUUID())
                    .firstName("a")
                    .lastName("b")
                    .email("c")
                    .phone("d")
                    .build();

            User actual = con.toUser(dto);

            assertNotNull(actual.getUuid());
            assertEquals(actual.getFirstName(), expected.getFirstName());
            assertEquals(actual.getLastName(), expected.getLastName());
            assertEquals(actual.getEmail(), expected.getEmail());
            assertEquals(actual.getPhone(), expected.getPhone());
        }
    }

    @Nested
    class ToUserFromUpdate {
        @Test
        void returns_existing_user() {
            UserUpdateDto dto = UserUpdateDto.builder().build();
            UUID uuid = UUID.randomUUID();

            User expected = User
                    .builder()
                    .uuid(uuid)
                    .firstName("a")
                    .lastName("b")
                    .email("c")
                    .phone("d")
                    .build();

            when(repo.findByUuid(uuid)).thenReturn(expected);

            User actual = con.toUser(uuid, dto);
            assertEquals(actual, expected);
        }

        static UUID uuid1 = UUID.randomUUID();

        @ParameterizedTest
        @MethodSource("g_converts_existing_user")
        void converts_existing_user(UserUpdateDto dto, User expected) {
            when(repo.findByUuid(uuid1)).thenReturn(expected);
            User actual = con.toUser(uuid1, dto);
            assertEquals(actual, expected);
        }

        static Stream<Arguments> g_converts_existing_user() {
            return Stream.of(
                    Arguments.of(
                            UserUpdateDto.builder().build(),
                            User.builder().uuid(uuid1).firstName("a").lastName("b").email("c").phone("d").build()),
                    Arguments.of(
                            UserUpdateDto.builder().firstName("1").build(),
                            User.builder().uuid(uuid1).firstName("1").lastName("b").email("c").phone("d").build()),
                    Arguments.of(
                            UserUpdateDto.builder().lastName("1").build(),
                            User.builder().uuid(uuid1).firstName("a").lastName("1").email("c").phone("d").build()),
                    Arguments.of(
                            UserUpdateDto.builder().email("1").build(),
                            User.builder().uuid(uuid1).firstName("a").lastName("b").email("1").phone("d").build()),
                    Arguments.of(
                            UserUpdateDto.builder().phone("1").build(),
                            User.builder().uuid(uuid1).firstName("a").lastName("b").email("c").phone("1").build()),
                    Arguments.of(
                            UserUpdateDto.builder().firstName("1").lastName("2").email("3").phone("4").build(),
                            User.builder().uuid(uuid1).firstName("1").lastName("2").email("3").phone("4").build())
            );
        }
    }
}
