package com.tech.agendaai.company.model.company;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public record CreateCompanyRequest(@NotEmpty
                               @Min(value = 3, message = "It should contain at least 3 characters")
                               String name,
                                   @NotEmpty
                               @Min(value = 3, message = "It should contain at least 3 characters")
                               String nickname,
                                   @NotEmpty(message = "You must chose a plan")
                               String plan
) {
}
