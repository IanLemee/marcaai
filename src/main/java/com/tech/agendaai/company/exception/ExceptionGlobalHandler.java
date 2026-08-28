package com.tech.agendaai.company.exception;

import com.tech.agendaai.company.model.appointment.AppointmentAlreadyTakenException;
import com.tech.agendaai.company.model.company.CompanyNotFoundException;
import com.tech.agendaai.company.model.operatingHours.OperatingDayDontExistException;
import com.tech.agendaai.company.model.user.UserAlreadyExistException;
import com.tech.agendaai.company.model.user.UserNotFoundException;
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

    @ExceptionHandler(AppointmentAlreadyTakenException.class)
    public ResponseEntity<Object> appointmentAlreadyTakenException(AppointmentAlreadyTakenException e, HttpServletRequest request) {
        HttpStatus conflict = HttpStatus.CONFLICT;
        ErrorResponse errorResponse = getErrorResponse(e, request, conflict);
        return new ResponseEntity<>(errorResponse, conflict);
    }

    @ExceptionHandler(OperatingDayDontExistException.class)
    public ResponseEntity<Object> operatingDayDontExistException(OperatingDayDontExistException e, HttpServletRequest request) {
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        ErrorResponse errorResponse = getErrorResponse(e, request, notFound);
        return new ResponseEntity<>(errorResponse, notFound);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> employeeNotFound(UserNotFoundException e, HttpServletRequest request) {
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        ErrorResponse errorResponse = getErrorResponse(e, request, notFound);
        return new ResponseEntity<>(errorResponse, notFound);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<Object> userAlreadyExistException(UserAlreadyExistException exception, HttpServletRequest request) {
        HttpStatus conflict = HttpStatus.CONFLICT;
        ErrorResponse errorResponse = getErrorResponse(exception, request, conflict);
        return new ResponseEntity<>(errorResponse, conflict);
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
