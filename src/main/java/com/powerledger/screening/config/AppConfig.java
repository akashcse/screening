package com.powerledger.screening.config;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.modelmapper.convention.MatchingStrategies.STRICT;

/**
 * All required beans for this application
 * is defined here
 */
@Configuration
@Slf4j
public class AppConfig {
    /**
     * Define ModelMapper bean for
     * globally use in application
     * @return ModelMapper
     */
    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(STRICT);
        return modelMapper;
    }
}
