package cz.cvut.moviemate.userservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistrationCustomizer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class MyConsulRegCustomizer implements ConsulRegistrationCustomizer {


    @Value("${spring.cloud.consul.discovery.traefik-tags}")
    private String rawTags;

    @Override
    public void customize(ConsulRegistration registration) {
        List<String> tags = registration.getService().getTags();
        if (tags == null) {
            tags = new ArrayList<>();
        }

        // Parse custom tags from MOVIE_MATE_USER_SERVICE_LB_TAGS, splitting by newline
        if (rawTags != null && !rawTags.isBlank()) {
            String[] customTags = rawTags.split("\\n");
            for (String tag : customTags) {
                if (!tag.isBlank()) {
                    tags.add(tag.trim());
                }
            }
        }

        // Set back to registration in case the tags list was replaced
        registration.getService().setTags(tags);
    }
}
