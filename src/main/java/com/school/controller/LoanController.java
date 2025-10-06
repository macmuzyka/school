package com.school.controller;

import org.camunda.bpm.engine.RuntimeService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/loan")
public class LoanController {

    private final RuntimeService runtimeService;

    public LoanController(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @PostMapping("/apply")
    public String applyForLoan(@RequestBody Map<String, Object> request) {
        runtimeService.startProcessInstanceByKey("LoanProcess", request);
        return "Loan process started!";
    }
}
