package com.scottlogic.grad_training.friendface.sample_work;

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
