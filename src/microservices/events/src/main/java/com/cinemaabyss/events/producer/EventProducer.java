package com.cinemaabyss.events.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class EventProducer {

    private static final Logger log = LoggerFactory.getLogger(EventProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public EventProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(String topic, Map<String, Object> data) {
        String eventId = UUID.randomUUID().toString();

        Map<String, Object> envelope = new LinkedHashMap<>();
        envelope.put("eventId", eventId);
        envelope.put("topic", topic);
        envelope.put("timestamp", Instant.now().toString());
        envelope.put("data", data);

        try {
            String payload = objectMapper.writeValueAsString(envelope);
            kafkaTemplate.send(topic, eventId, payload);
            log.info("Published event {} to topic {}", eventId, topic);
        } catch (Exception e) {
            log.error("Failed to publish event to topic [{}]: {}", topic, e.getMessage(), e);
            throw new RuntimeException("Failed to publish event", e);
        }
    }
}
