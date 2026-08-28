package com.tech.agendaai.company.model.user;

import com.tech.agendaai.company.exception.BaseAppException;

public class UserNotFoundException extends BaseAppException {

    public UserNotFoundException() {
        super("User not found");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
