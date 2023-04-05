package com.jet.employeeapi.infrastructure.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {

    @KafkaListener(topics = "topic", groupId = "group-id")
    public void consume(String message) {
        log.info(String.format("Message received -> %s", message));
    }
}
