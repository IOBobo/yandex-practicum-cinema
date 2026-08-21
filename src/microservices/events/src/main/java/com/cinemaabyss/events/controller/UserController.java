package com.cinemaabyss.events.controller;

import com.cinemaabyss.events.producer.EventProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/events/user")
public class UserController {

    private static final String TOPIC = "user-events";

    private final EventProducer eventProducer;

    public UserController(EventProducer eventProducer) {
        this.eventProducer = eventProducer;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createUserEvent(@RequestBody Map<String, Object> body) {
        eventProducer.publish(TOPIC, body);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("status", "success", "topic", TOPIC));
    }
}
