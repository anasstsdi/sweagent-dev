package com.example.demo.controller;

import com.example.demo.dto.UserInput;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST surface for users. Thin: it only maps HTTP to {@link UserService} calls.
 * A missing id yields 404 (raised in the service); an invalid body yields 400 (bean validation).
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /** List every user. */
    @GetMapping
    public List<User> list() {
        return userService.list();
    }

    /** Fetch one user by id. */
    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        return userService.get(id);
    }

    /** Create a user. Returns 201 with the persisted row (id assigned by the database). */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody UserInput input) {
        return userService.create(input);
    }

    /** Replace name + email of an existing user. */
    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @Valid @RequestBody UserInput input) {
        return userService.update(id, input);
    }

    /** Delete a user. Returns 204. */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
