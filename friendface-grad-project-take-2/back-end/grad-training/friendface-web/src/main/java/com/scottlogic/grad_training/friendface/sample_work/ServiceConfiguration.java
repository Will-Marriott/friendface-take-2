package com.scottlogic.grad_training.friendface.sample_work;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {
    @Bean
    public EmployeeOfTheMonthService employeeOfTheMonthService() {
        return new EmployeeOfTheMonthService(true);
    }
}

