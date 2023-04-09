package com.portfolijo.schedjoeler.service;

import com.portfolijo.schedjoeler.domain.User;
import com.portfolijo.schedjoeler.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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
class UserServiceTest {
    UserRepository repo;
    UserService svc;

    @BeforeEach
    void setup() {
        repo = mock(UserRepository.class);
        svc = new UserService(repo);
    }

    @Nested
    class AssertUserExists {
        @Test
        void does_not_throw_exception_if_user_exists() {
            UUID uuid = UUID.randomUUID();
            when(repo.existsByUuid(uuid)).thenReturn(true);
            assertDoesNotThrow(() -> svc.assertUserExists(uuid));
        }

        @Test
        void throws_NoSuchElementException_if_user_does_not_exist() {
            when(repo.existsByUuid(any(UUID.class))).thenReturn(false);
            assertThrows(NoSuchElementException.class, () -> svc.assertUserExists(UUID.randomUUID()));
        }
    }

    @Nested
    class FindOne {
        @Test
        void can_find_by_uuid() {
            UUID uuid = UUID.randomUUID();
            User expected = User
                    .builder()
                    .id(1L)
                    .uuid(uuid)
                    .firstName("a")
                    .lastName("b")
                    .email("c")
                    .phone("d")
                    .build();
            when(repo.findByUuid(uuid)).thenReturn(expected);
            User actual = svc.findOne(uuid);
            assertEquals(actual, expected);
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
            List<User> expected = List.of(
                    User.builder().id(1L).uuid(UUID.randomUUID()).firstName("a").lastName("b").email("c").phone("d").build(),
                    User.builder().id(2L).uuid(UUID.randomUUID()).firstName("a").lastName("b").email("c").phone("d").build(),
                    User.builder().id(3L).uuid(UUID.randomUUID()).firstName("a").lastName("b").email("c").phone("d").build());

            when(repo.findAll()).thenReturn(expected);
            List<User> actual = svc.findAll();
            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }

        @Test
        void returns_empty_list_when_none_found() {
            List<User> expected = List.of();
            when(repo.findAll()).thenReturn(expected);
            List<User> actual = svc.findAll();
            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }
    }

    @Nested
    class SaveOne {
        @Test
        void returns_saved_user() {
            User expected = User
                    .builder()
                    .id(1L)
                    .uuid(UUID.randomUUID())
                    .firstName("a")
                    .lastName("b")
                    .email("c")
                    .phone("d")
                    .build();
            when(repo.save(expected)).thenReturn(expected);
            User actual = svc.saveOne(expected);
            assertEquals(actual, expected);
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
        void throws_NoSuchElementException_if_user_does_not_exist() {
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
