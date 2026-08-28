package com.tech.agendaai.company.documentation;

import com.tech.agendaai.company.model.appointment.AppointmentResponse;
import com.tech.agendaai.company.model.appointment.CreateAppointment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface AppointmentControllerDocs {

    @Operation(summary = "Cadastrar um horario", description = "Criar um novo horario na agenda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Horario é cadastrado com sucesso",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AppointmentResponse.class))}
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Horario selecionado já está ocupado"
            )

    })
    public ResponseEntity<AppointmentResponse> createAppointment(CreateAppointment appointment, @PathVariable String companyNickname);
}
