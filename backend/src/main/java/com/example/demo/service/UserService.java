package com.example.demo.service;

import com.example.demo.dto.UserInput;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Business layer for users. Talks to the database only through {@link UserRepository}
 * (Spring Data JPA over the in-memory H2 datasource configured in application.properties).
 */
@Service
public class UserService {

    private final UserRepository users;

    public UserService(UserRepository users) {
        this.users = users;
    }

    @Transactional(readOnly = true)
    public List<User> list() {
        return users.findAll();
    }

    @Transactional(readOnly = true)
    public User get(Long id) {
        return users.findById(id).orElseThrow(() -> notFound(id));
    }

    @Transactional
    public User create(UserInput input) {
        return users.save(new User(input.name(), input.email()));
    }

    @Transactional
    public User update(Long id, UserInput input) {
        User user = users.findById(id).orElseThrow(() -> notFound(id));
        user.setName(input.name());
        user.setEmail(input.email());
        return users.save(user);
    }

    @Transactional
    public void delete(Long id) {
        if (!users.existsById(id)) {
            throw notFound(id);
        }
        users.deleteById(id);
    }

    private static ResponseStatusException notFound(Long id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "user " + id + " not found");
    }
}
