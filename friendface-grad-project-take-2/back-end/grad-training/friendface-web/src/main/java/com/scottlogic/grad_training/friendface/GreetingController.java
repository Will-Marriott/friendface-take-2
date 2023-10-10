package com.scottlogic.grad_training.friendface;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/greetings")
public class GreetingController {

    @GetMapping
    public String index() {
        return "Sup";
    }

    @GetMapping("/{locale}")
    public String localized(@PathVariable String locale) {
        if (locale.equals("fr")) {
            return "Bonjour";
        }
        return "Hello";
    }

    @GetMapping("/employee")
    public Employee employeeOfTheMonth() {
        return employeeOfTheMonthService.computeEmployeeOfTheMonth();
    }


    @PostMapping("/welcome")
    public String welcome(@RequestBody Employee employee) {
        return String.format("Welcome %s!", employee.getName());
    }

    private final EmployeeOfTheMonthService employeeOfTheMonthService;

    public GreetingController(EmployeeOfTheMonthService employeeOfTheMonthService) {
        this.employeeOfTheMonthService = employeeOfTheMonthService;
    }


}