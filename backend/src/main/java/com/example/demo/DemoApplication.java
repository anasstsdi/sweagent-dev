package com.example.demo;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    /** Seed a few users so the list endpoint returns something. */
    @Bean
    CommandLineRunner seed(UserRepository users) {
        return args -> {
            if (users.count() == 0) {
                users.save(new User("Alice Martin", "alice@example.com"));
                users.save(new User("Bob Durand", "bob@example.com"));
                users.save(new User("Chloe Petit", "chloe@example.com"));
                users.save(new User("David Nguyen", "david@example.com"));
                users.save(new User("Emma Leroy", "emma@example.com"));
            }
        };
    }
}
