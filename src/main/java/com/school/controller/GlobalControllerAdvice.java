package com.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.nio.file.AccessDeniedException;
import java.util.Map;

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

//    @ExceptionHandler(Exception.class)
//    public ModelAndView unhandledException(Exception e) {
//        log.info("Exception: {}", e.getMessage());
//        ModelAndView mav = new ModelAndView("iae-error-page");
//        mav.addObject("message", "Exception: " + e.getMessage());
//        return mav;
//    }

//    @ExceptionHandler(Exception.class)
//    public ModelAndView unhandledException(AccessDeniedException ade) {
//        log.info("Exception: {}", ade.getMessage());
//        ModelAndView mav = new ModelAndView("iae-error-page");
//        mav.addObject("message", "Exception: " + ade.getMessage());
//        return mav;
//    }

//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<Map<String, String>> handleAccessDenied(AccessDeniedException ex) {
//        return ResponseEntity
//                .status(HttpStatus.FORBIDDEN)
//                .body(Map.of("Access denied!", "You do not have the required role to perform this action."));
//    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedView() {
        log.error("=== handleAccessDeniedView() ===");
        return "error/forbidden"; // maps to templates/error/forbidden.html
    }
}
