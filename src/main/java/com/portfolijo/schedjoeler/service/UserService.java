package com.portfolijo.schedjoeler.service;

import com.portfolijo.schedjoeler.domain.User;
import com.portfolijo.schedjoeler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Provides services for handling {@link User} entities
 */
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository REPO;

    /**
     * Verifies that the identified user exists
     *
     * @param uuid A user uuid
     * @throws NoSuchElementException If the user does not exist
     */
    public void assertUserExists(UUID uuid) throws NoSuchElementException {
        if (!REPO.existsByUuid(uuid))
            handleUserNotFound(uuid);
    }

    /**
     * Finds the identified user
     *
     * @param uuid A user uuid
     * @return A user
     * @throws NoSuchElementException If the user does not exist
     */
    public User findOne(UUID uuid) throws NoSuchElementException {
        User user = REPO.findByUuid(uuid);

        if (user == null)
            handleUserNotFound(uuid);

        return user;
    }

    /**
     * Finds all users
     *
     * @return A list of users
     */
    public List<User> findAll() {
        return REPO.findAll();
    }

    /**
     * Saves the given user
     *
     * @param user A user
     * @return The saved user
     */
    @Transactional
    @Modifying
    public User saveOne(User user) {
        return REPO.save(user);
    }

    /**
     * Deletes the identified user
     *
     * @param uuid A user uuid
     * @throws NoSuchElementException If the user does not exist
     * @throws Exception If the deletion was unsuccessful
     */
    @Transactional
    @Modifying
    public void deleteOne(UUID uuid) throws Exception {
        assertUserExists(uuid);

        if (REPO.deleteByUuid(uuid) == 0)
            throw new Exception("Unable to delete user uuid="+uuid+".");
    }

    /**
     * Throws an exception indicating that no user was found with the given uuid
     *
     * @param uuid A user uuid
     * @throws NoSuchElementException Unconditionally
     */
    private void handleUserNotFound(UUID uuid) throws NoSuchElementException {
        throw new NoSuchElementException("User uuid="+uuid+" not found.");
    }
}
