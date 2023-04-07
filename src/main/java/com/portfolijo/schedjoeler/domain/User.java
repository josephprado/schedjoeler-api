package com.portfolijo.schedjoeler.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.util.UUID;

/**
 * Represents an application user
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Entity
@Table(name = "sj_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true)
    @NotNull
    private UUID uuid;

    @PrePersist
    private void initUuid() {
        if (uuid == null)
            uuid = UUID.randomUUID();
    }

    @Column(name = "first_name", nullable = false)
    @NotBlank
    @NonNull
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotBlank
    @NonNull
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;
}
