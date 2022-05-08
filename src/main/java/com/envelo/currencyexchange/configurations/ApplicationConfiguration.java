package com.envelo.currencyexchange.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfiguration {

    /**
     * Method used to create @Bean of {@link RestTemplate} to be managed by Spring Framework.
     *
     * @return RestTemplate bean.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
