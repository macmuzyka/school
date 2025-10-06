package com.school.model.camunda;

public record LoanApprovedEvent(String processInstanceId, String assignee) {
}
