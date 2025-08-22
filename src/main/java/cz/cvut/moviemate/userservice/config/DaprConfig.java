package cz.cvut.moviemate.userservice.config;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.spring.messaging.DaprMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaprConfig {
    @Bean
    public DaprMessagingTemplate<Object> daprMessagingTemplate(@Autowired DaprClient daprClient,
                                                               @Value("${dapr.pubsub.component}") String pubsubName) {
        return new DaprMessagingTemplate<>(daprClient, pubsubName, true);
    }
}


