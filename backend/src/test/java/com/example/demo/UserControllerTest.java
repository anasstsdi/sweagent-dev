package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Each test runs in its own transaction that is rolled back afterwards, so mutations
 * (create / update / delete) do not leak into the next test. The 5 seeded users
 * (DemoApplication#seed) are committed once at startup and always visible.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void listReturnsSeededUsers() throws Exception {
        mvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].name").value("Alice Martin"));
    }

    @Test
    void getReturnsOneUser() throws Exception {
        mvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void getUnknownUserIs404() throws Exception {
        mvc.perform(get("/api/users/9999")).andExpect(status().isNotFound());
    }

    @Test
    void createThenListHasOneMore() throws Exception {
        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Frank Ocean\",\"email\":\"frank@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Frank Ocean"));

        mvc.perform(get("/api/users")).andExpect(jsonPath("$.length()").value(6));
    }

    @Test
    void updateChangesFields() throws Exception {
        mvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Alice M.\",\"email\":\"alice.m@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice M."))
                .andExpect(jsonPath("$.email").value("alice.m@example.com"));
    }

    @Test
    void deleteRemovesUser() throws Exception {
        mvc.perform(delete("/api/users/2")).andExpect(status().isNoContent());
        mvc.perform(get("/api/users")).andExpect(jsonPath("$.length()").value(4));
    }

    @Test
    void createWithInvalidEmailIs400() throws Exception {
        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"No Email\",\"email\":\"not-an-email\"}"))
                .andExpect(status().isBadRequest());
    }
}
