package com.scottlogic.grad_training.friendface;

import org.springframework.stereotype.Service;

public class EmployeeOfTheMonthService {
    private final boolean formatNamesInUpperCase;

    public EmployeeOfTheMonthService(boolean formatNamesInUpperCase) {
        this.formatNamesInUpperCase = formatNamesInUpperCase;
    }

    public Employee computeEmployeeOfTheMonth() {
        final var employee = new Employee();
        final var nameNominal = System.getProperty("user.name");
        final var nameFormatted = formatNamesInUpperCase
                ? nameNominal.toUpperCase()
                : nameNominal;
        employee.setName(nameFormatted);
        return employee;
    }
}
