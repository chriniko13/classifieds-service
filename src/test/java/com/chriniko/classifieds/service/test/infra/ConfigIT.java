package com.chriniko.classifieds.service.test.infra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@Profile("int")
@Configuration
public class ConfigIT {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
