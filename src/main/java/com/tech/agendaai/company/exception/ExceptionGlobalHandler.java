package com.tech.agendaai.company.exception;

import com.tech.agendaai.company.model.company.CompanyNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionGlobalHandler {

    @ExceptionHandler(CompanyNotFoundException.class)
    public ResponseEntity<Object> companyNotFoundException(CompanyNotFoundException companyNotFoundException, HttpServletRequest request) {
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        ErrorResponse errorResponse = getErrorResponse(companyNotFoundException, request, notFound);
        return new ResponseEntity<>(errorResponse, notFound);
    }

    @ExceptionHandler(BaseAppException.class)
    public ResponseEntity<Object> baseException(BaseAppException exception, HttpServletRequest request) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ErrorResponse errorResponse = getErrorResponse(exception, request, badRequest);
        return new ResponseEntity<>(errorResponse, badRequest);
    }

    private static @NonNull ErrorResponse getErrorResponse(BaseAppException exception, HttpServletRequest request, HttpStatus httpStatus) {
        return new ErrorResponse(
                httpStatus.toString(), httpStatus.value(), exception.getMessage(), request.getRequestURI()
        );
    }
}
