package com.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalControllerAdvice {
    private final Logger log = LoggerFactory.getLogger(GlobalControllerAdvice.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleIllegalArgumentException(IllegalArgumentException iae) {
        log.info("handleIllegalArgumentException: {}", iae.getMessage());
        ModelAndView mav = new ModelAndView("not-found-error-page");
        mav.addObject("message", "Missing resource: " + iae.getMessage());
        return mav;
    }
}
