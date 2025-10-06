package com.school.service.camunda;

import org.springframework.stereotype.Service;

@Service
public class LoanDecisionService {
    public boolean evaluateLoan(int amount, int income) {
        return income >= (amount / 2);
    }
}
