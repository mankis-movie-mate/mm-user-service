package cz.cvut.moviemate.userservice.service.impl;

import io.dapr.spring.messaging.DaprMessagingTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j // <-- This adds the 'log' object
@Service
@RequiredArgsConstructor
public class DaprEventPublisher {

    private final DaprMessagingTemplate<Object> daprMessagingTemplate;

    @Value("${dapr.pubsub.topic.login}")
    private String loginTopic;

    @Value("${dapr.pubsub.topic.register}")
    private String registerTopic;

    public void publishLoginEvent(Object event) {
        try {
            daprMessagingTemplate.send(loginTopic, event);
            log.info("Published login event to topic: {}", loginTopic);
        } catch (Exception e) {
            log.error("Failed to publish login event", e);
        }
    }

    public void publishRegisterEvent(Object event) {
        try {
            daprMessagingTemplate.send(registerTopic, event);
            log.info("Published register event to topic: {}", registerTopic);
        } catch (Exception e) {
            log.error("Failed to publish register event", e);
        }
    }
}
