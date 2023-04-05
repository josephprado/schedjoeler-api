package com.portfolijo.schedjoeler.service;

import com.portfolijo.schedjoeler.domain.User;
import com.portfolijo.schedjoeler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Provides services for handling {@link User} entities
 */
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository REPO;
}
