package com.tech.agendaai.company.model.user;

import com.tech.agendaai.company.exception.BaseAppException;

public class UserAlreadyExistException extends BaseAppException {
    public UserAlreadyExistException() {
        super("User already exist");
    }
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
