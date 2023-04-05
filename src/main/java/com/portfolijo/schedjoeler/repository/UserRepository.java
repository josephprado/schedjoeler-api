package com.portfolijo.schedjoeler.repository;

import com.portfolijo.schedjoeler.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Provides CRUD operations for {@link User} entities
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
