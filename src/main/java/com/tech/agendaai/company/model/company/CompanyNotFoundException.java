package com.tech.agendaai.company.model.company;

import com.tech.agendaai.company.exception.BaseAppException;

public class CompanyNotFoundException extends BaseAppException {

    public CompanyNotFoundException(){
        super("Company not found by nickname");
    }

    public CompanyNotFoundException(String message) {
        super(message);
    }
}
