package com.school.tasklistener;

import com.school.model.camunda.LoanApprovedEvent;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class LoanApprovalListener implements TaskListener {
    public static final Logger log = LoggerFactory.getLogger(LoanApprovalListener.class);
    private final ApplicationEventPublisher eventPublisher;

    public LoanApprovalListener(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("delegateTask.getEventName(): {}", delegateTask.getEventName());
        if ("complete".equals(delegateTask.getEventName())) {
            eventPublisher.publishEvent(new LoanApprovedEvent(delegateTask.getProcessInstanceId(), delegateTask.getAssignee()));
        } else {
            log.info("DISMISSED");
        }
    }
}
