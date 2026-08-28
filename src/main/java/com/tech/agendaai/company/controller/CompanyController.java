package com.tech.agendaai.company.controller;

import com.tech.agendaai.company.model.company.RegisterCompanyRequest;
import com.tech.agendaai.company.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/create")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    public void create(@RequestBody @Valid RegisterCompanyRequest registerCompanyRequest) {
        companyService.create(registerCompanyRequest);
    }

//    @GetMapping
//    public String greet(HttpServletRequest request) {
//        String authorization = request.getHeader("Authorization").substring(0,7);
//        Jwt build = Jwt.withTokenValue(authorization).build();
//        Map<String, Object> claims = build.getClaims();
//        System.out.println(claims.get("name"));
//        return "hello ";
//    }

    @GetMapping("/{nickname}")
    public boolean isNicknameAvailable(@PathVariable String nickname) {
        return false;
    }

    @PatchMapping("change/plan/{nickname}")
    public ResponseEntity<Void> changePlan(@PathVariable String nickname, String plan) {
        companyService.changePlan(nickname, plan);
        return null;
    }
}
