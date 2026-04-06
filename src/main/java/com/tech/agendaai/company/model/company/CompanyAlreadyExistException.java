package com.tech.agendaai.company.model.company;

import com.tech.agendaai.company.exception.BaseAppException;

public class CompanyAlreadyExistException extends BaseAppException {
    public CompanyAlreadyExistException() {
        super("Company already exist");
    }
    public CompanyAlreadyExistException(String message) {
        super(message);
    }
}
