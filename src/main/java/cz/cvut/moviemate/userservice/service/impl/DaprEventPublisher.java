package cz.cvut.moviemate.userservice.service.impl;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import lombok.extern.slf4j.Slf4j; // Add Lombok's logger annotation!
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j // <-- This adds the 'log' object
@Service
public class DaprEventPublisher {

    private final DaprClient daprClient = new DaprClientBuilder().build();

    private final String pubsubName;
    private final String loginTopic;
    private final String registerTopic;

    // Values injected from Spring config
    public DaprEventPublisher(
            @Value("${dapr.pubsub.component}") String pubsubName,
            @Value("${dapr.pubsub.topic.login}") String loginTopic,
            @Value("${dapr.pubsub.topic.register}") String registerTopic) {
        this.pubsubName = pubsubName;
        this.loginTopic = loginTopic;
        this.registerTopic = registerTopic;
    }

    public void publishLoginEvent(Object event) {
        try {
            daprClient.publishEvent(pubsubName, loginTopic, event).block();
            log.info("Published login event to Dapr/Kafka, topic={}", loginTopic);
        } catch (Exception e) {
            log.error("Failed to publish login event to Dapr/Kafka", e);
        }
    }

    public void publishRegisterEvent(Object event) {
        try {
            daprClient.publishEvent(pubsubName, registerTopic, event).block();
            log.info("Published register event to Dapr/Kafka, topic={}", registerTopic);
        } catch (Exception e) {
            log.error("Failed to publish register event to Dapr/Kafka", e);
        }
    }
}
