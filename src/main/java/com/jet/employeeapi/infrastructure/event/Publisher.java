package com.jet.employeeapi.infrastructure.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class Publisher {

    private final ApplicationEventPublisher publisher;

    public void publishEvent(String message) {
        log.info("Publishing employee event.");
        publisher.publishEvent(new EmployeeEvent(this, message));
    }
}
