package com.school.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

//TODO: further develop
@ControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleIllegalArgumentException(IllegalArgumentException iae) {
        ModelAndView mav = new ModelAndView("iae-error-page");
        mav.addObject("message", "Illegal Argument Exception: " + iae.getMessage());
        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView unhandledException(Exception e) {
        ModelAndView mav = new ModelAndView("iae-error-page");
        mav.addObject("message", "Unhandled exception: " + e.getMessage());
        return mav;
    }
}
