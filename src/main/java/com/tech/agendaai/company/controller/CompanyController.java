package com.tech.agendaai.company.controller;

import com.tech.agendaai.company.model.company.RegisterCompanyRequest;
import com.tech.agendaai.company.service.CompanyService;
import jakarta.validation.Valid;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.core.oidc.OidcIdToken;
//import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
//import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
//import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.oauth2.jwt.Jwt;
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
    public boolean isNicknameAvaliable(@PathVariable String nickname) {
        return false;
    }


}
