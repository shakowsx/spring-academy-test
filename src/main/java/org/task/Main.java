package org.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class of the Spring Boot application, which starts the application.
 *
 * @author Evgeniy Ryabov
 * @version 1.0
 * @since 23-03-2024
 */
@SpringBootApplication
public class Main {

    /**
     * Main method that starts the Spring Boot application.
     *
     * @param args Command-line arguments that can be passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}