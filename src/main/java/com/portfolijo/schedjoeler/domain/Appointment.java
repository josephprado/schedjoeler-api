package com.portfolijo.schedjoeler.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a scheduled appointment between a provider and client
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Entity
@Table(name = "sj_appointment")
public class Appointment {

    @RequiredArgsConstructor
    @Getter
    public enum Status {
        NEW,
        RESCHEDULED,
        COMPLETE,
        CANCELLED
    }

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

    @Column(name = "date_time", nullable = false)
    @NotNull
    @NonNull
    private LocalDateTime dateTime;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(
            name = "provider_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk__appointment__provider_id"),
            nullable = false
    )
    @NotNull
    @NonNull
    private User provider;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(
            name = "client_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk__appointment__client_id"),
            nullable = false
    )
    @NotNull
    @NonNull
    private User client;

    @Column(name = "status", nullable = false)
    @NotNull
    @NonNull
    private Status status;

    @Column(name = "location")
    private String location;

    @Column(name = "description")
    private String description;
}
