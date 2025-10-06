package com.school.delegate;

import com.school.service.camunda.LoanDecisionService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class LoanDecisionDelegate implements JavaDelegate {

    private final LoanDecisionService loanDecisionService;

    public LoanDecisionDelegate(LoanDecisionService loanDecisionService) {
        this.loanDecisionService = loanDecisionService;
    }

    @Override
    public void execute(DelegateExecution execution) {
        Integer amount = (Integer) execution.getVariable("loanAmount");
        Integer income = (Integer) execution.getVariable("applicantIncome");

        boolean approved = loanDecisionService.evaluateLoan(amount, income);
        execution.setVariable("approved", approved);

        System.out.println("Loan evaluated: " + (approved ? "APPROVED" : "REJECTED"));
    }
}
