package com.portfolijo.schedjoeler.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolijo.schedjoeler.converter.UserConverter;
import com.portfolijo.schedjoeler.domain.User;
import com.portfolijo.schedjoeler.dto.UserCreateDto;
import com.portfolijo.schedjoeler.dto.UserDto;
import com.portfolijo.schedjoeler.dto.UserUpdateDto;
import com.portfolijo.schedjoeler.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService svc;

    @MockBean
    UserConverter con;

    final String BASE_URL = "/api/users";

    /**
     * Converts the given user DTO to a string comparable to the response content of an MvcResult
     *
     * @param dto A user DTO
     * @return A string representation of the user DTO
     */
    String convertUserDtoToString(UserDto dto) {
        return dto.toString().replace("UserDto(", "{").replace(")", "}");
    }

    /**
     * Converts the given list of user DTOs to a string comparable to the response content of an MvcResult
     *
     * @param dtos A list of user DTOs
     * @return A string representation of the list of user DTOs
     */
    String convertUserDtoToString(List<UserDto> dtos) {
        return dtos.toString().replace("UserDto(", "{").replace(")", "}");
    }

    @Nested
    class GetOne {
        @Test
        void returns_status_ok_on_success() throws Exception {
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

            when(svc.findOne(uuid)).thenReturn(user);
            when(con.toDto(user)).thenReturn(expected);

            mvc.perform(get(BASE_URL+"/"+uuid))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void returns_expected_user_dto() throws Exception {
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

            UserDto expectedDto = UserDto
                    .builder()
                    .uuid(uuid)
                    .firstName("a")
                    .lastName("b")
                    .email("c")
                    .phone("d")
                    .build();

            when(svc.findOne(uuid)).thenReturn(user);
            when(con.toDto(user)).thenReturn(expectedDto);

            MvcResult result = mvc.perform(get(BASE_URL+"/"+uuid)).andReturn();
            var response = mapper.readValue(result.getResponse().getContentAsString(), Response.class);

            // TODO: figure out how to compare these as UserDtos instead of strings
            String actual = response.getData().get(0).toString();
            String expected = convertUserDtoToString(expectedDto);
            assertEquals(expected, actual);
        }
    }

    @Nested
    class GetAll {
        @Test
        void returns_status_ok_on_success() throws Exception {
            List<User> users = new ArrayList<>();
            UUID uuid = UUID.randomUUID();

            for (int i = 0; i < 3; i++) {
                users.add(User
                        .builder()
                        .id((long) i)
                        .uuid(uuid)
                        .firstName("a")
                        .lastName("b")
                        .email("c")
                        .phone("d")
                        .build());
            }

            UserDto expectedDto = UserDto
                    .builder()
                    .uuid(uuid)
                    .firstName("a")
                    .lastName("b")
                    .email("c")
                    .phone("d")
                    .build();

            when(svc.findAll()).thenReturn(users);
            when(con.toDto(any(User.class))).thenReturn(expectedDto);

            mvc.perform(get(BASE_URL))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void returns_expected_list_of_users() throws Exception {
            List<User> users = new ArrayList<>();
            List<UserDto> expectedDtos = new ArrayList<>();
            UUID uuid = UUID.randomUUID();

            for (int i = 0; i < 3; i++) {
                users.add(User
                        .builder()
                        .id((long) i)
                        .uuid(uuid)
                        .firstName("a")
                        .lastName("b")
                        .email("c")
                        .phone("d")
                        .build());
                expectedDtos.add(UserDto
                        .builder()
                        .uuid(uuid)
                        .firstName("a")
                        .lastName("b")
                        .email("c")
                        .phone("d")
                        .build());
            }

            when(svc.findAll()).thenReturn(users);
            when(con.toDto(any(User.class))).thenReturn(expectedDtos.get(0));

            MvcResult result = mvc.perform(get(BASE_URL)).andReturn();
            var response = mapper.readValue(result.getResponse().getContentAsString(), Response.class);

            // TODO: figure out how to compare these as UserDtos instead of strings
            String actual = response.getData().toString();
            String expected = convertUserDtoToString(expectedDtos);
            assertEquals(expected, actual);
        }
    }

    @Nested
    class SaveOne {
        @Test
        void returns_status_created_on_success() throws Exception {
            User user = User
                    .builder()
                    .firstName("a")
                    .lastName("b")
                    .email("c")
                    .phone("d")
                    .build();

            UserDto expectedDto = UserDto
                    .builder()
                    .uuid(UUID.randomUUID())
                    .firstName("a")
                    .lastName("b")
                    .email("c")
                    .phone("d")
                    .build();

            when(con.toUser(any(UserCreateDto.class))).thenReturn(user);
            when(svc.saveOne(user)).thenReturn(user);
            when(con.toDto(user)).thenReturn(expectedDto);

            String requestBody = mapper.writeValueAsString(user);
            mvc.perform(post(BASE_URL)
                    .content(requestBody)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());
        }

        @Test
        void returns_expected_user_dto() throws Exception {
            User user = User
                    .builder()
                    .firstName("a")
                    .lastName("b")
                    .email("c")
                    .phone("d")
                    .build();

            UserDto expectedDto = UserDto
                    .builder()
                    .uuid(UUID.randomUUID())
                    .firstName("a")
                    .lastName("b")
                    .email("c")
                    .phone("d")
                    .build();

            when(con.toUser(any(UserCreateDto.class))).thenReturn(user);
            when(svc.saveOne(user)).thenReturn(user);
            when(con.toDto(user)).thenReturn(expectedDto);

            String requestBody = mapper.writeValueAsString(user);
            MvcResult result = mvc
                    .perform(post(BASE_URL)
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            var response = mapper.readValue(result.getResponse().getContentAsString(), Response.class);

            // TODO: figure out how to compare these as UserDtos instead of strings
            String actual = response.getData().get(0).toString();
            String expected = convertUserDtoToString(expectedDto);
            assertEquals(expected, actual);
        }
    }

    @Nested
    class UpdateOne {
        @Test
        void returns_status_ok_on_success() throws Exception {
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

            UserDto expectedDto = UserDto
                    .builder()
                    .uuid(uuid)
                    .firstName("a")
                    .lastName("b")
                    .email("c")
                    .phone("d")
                    .build();

            when(con.toUser(eq(uuid), any(UserUpdateDto.class))).thenReturn(user);
            when(svc.saveOne(user)).thenReturn(user);
            when(con.toDto(user)).thenReturn(expectedDto);

            String requestBody = mapper.writeValueAsString(user);
            mvc.perform(patch(BASE_URL+"/"+uuid)
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        void returns_expected_user_dto() throws Exception {
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

            UserDto expectedDto = UserDto
                    .builder()
                    .uuid(uuid)
                    .firstName("a")
                    .lastName("b")
                    .email("c")
                    .phone("d")
                    .build();

            when(con.toUser(eq(uuid), any(UserUpdateDto.class))).thenReturn(user);
            when(svc.saveOne(user)).thenReturn(user);
            when(con.toDto(user)).thenReturn(expectedDto);

            String requestBody = mapper.writeValueAsString(user);
            MvcResult result = mvc
                    .perform(patch(BASE_URL+"/"+uuid)
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            var response = mapper.readValue(result.getResponse().getContentAsString(), Response.class);

            // TODO: figure out how to compare these as UserDtos instead of strings
            String actual = response.getData().get(0).toString();
            String expected = convertUserDtoToString(expectedDto);
            assertEquals(expected, actual);
        }
    }

    @Nested
    class DeleteOne {
        @Test
        void returns_status_no_content_on_success() throws Exception {
            mvc.perform(delete(BASE_URL+"/"+UUID.randomUUID()))
                    .andExpect(status().isNoContent());
        }
    }
}