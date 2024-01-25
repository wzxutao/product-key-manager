package cn.rypacker.productkeymanager.controllers;


import cn.rypacker.productkeymanager.dto.adminlisting.ErrorResponse;
import cn.rypacker.productkeymanager.exception.IdentifiedWebException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IdentifiedWebException.class)
    public ResponseEntity<ErrorResponse> handleIdentifiedWebException(IdentifiedWebException e) throws IOException {
        return ResponseEntity.badRequest().body(ErrorResponse
                .builder()
                .message(e.getMessage())
                .status(e.getStatus())
                .build());
    }
}
