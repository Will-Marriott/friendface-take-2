package com.scottlogic.grad_training.friendface;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {
    @Bean
    public EmployeeOfTheMonthService employeeOfTheMonthService() {
        return new EmployeeOfTheMonthService(true);
    }
}

