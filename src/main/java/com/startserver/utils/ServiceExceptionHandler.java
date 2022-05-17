package com.startserver.utils;

import com.startserver.DTO.RMessage;
import com.startserver.responseUtils.MessageExeption;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@EnableWebMvc
@ControllerAdvice
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    ResponseEntity<?> handleControllerException(HttpServletRequest req, Throwable ex) {
        RMessage message = new RMessage(ex.getMessage());
        if(ex instanceof MessageExeption){
            return ResponseEntity.status(600).body(message);
        }
        return new ResponseEntity<Object>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
