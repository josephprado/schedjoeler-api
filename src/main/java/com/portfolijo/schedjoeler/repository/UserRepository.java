package com.portfolijo.schedjoeler.repository;

import com.portfolijo.schedjoeler.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Provides CRUD operations for {@link User} entities
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Checks if the given uuid identifies an existing user
     *
     * @param uuid A user uuid
     * @return True if a user exists, or false otherwise
     */
    boolean existsByUuid(UUID uuid);

    /**
     * Finds the identified user
     *
     * @param uuid A user uuid
     * @return A user
     */
    User findByUuid(UUID uuid);

    /**
     * Deletes the identified user
     *
     * @param uuid A user uuid
     * @return The number of deleted records
     */
    int deleteByUuid(UUID uuid);
}
