package com.school.tasklistener;

import com.school.model.camunda.LoanApprovedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class LoanApprovalEventHandler {

    @EventListener
    public void handleLoanApproved(LoanApprovedEvent event) {
        System.out.println("User " + event.assignee() + " approved loan for process " + event.processInstanceId());
    }
}
