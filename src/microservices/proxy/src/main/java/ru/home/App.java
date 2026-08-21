package ru.home;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ProxyProperties.class)
public class App {
    public static final String MOVIES_PATHS = "/api/movies/**";
    public static final String WEIGHT_GROUP = "movies-traffic";

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
