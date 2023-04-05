package com.jet.employeeapi.infrastructure.event;

import com.jet.employeeapi.infrastructure.kafka.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class Listener implements ApplicationListener<EmployeeEvent> {

    private final KafkaProducerService producerService;

    @Override
    public void onApplicationEvent(EmployeeEvent event) {
        log.info("Received employee event.");
        producerService.sendMessage(event.getMessage());
    }
}
