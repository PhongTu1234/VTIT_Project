package com.vtit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vtit.utils.MessageUtil;

@Controller
public class LibraryProjectStarter {

    @RestController
    public static class HealthCheckController {

        private final MessageUtil messageUtil;
        
        public HealthCheckController(MessageUtil messageUtil) {
        	this.messageUtil = messageUtil;
        }

        @GetMapping
        public ResponseEntity<String> getMessage() {
            return ResponseEntity.ok(messageUtil.get("healthcheck.message"));
        }

    }
}
